package io.split.dbm.integrations;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

public class Tealium2Split implements HttpFunction {

	public static final String kEnvironmentName = "Prod-Default";
	public static final String kTrafficTypeName = "user";

	public void service(HttpRequest request, HttpResponse response) throws Exception {
		long start = System.currentTimeMillis();

		String environment = kEnvironmentName;
		Map<String, List<String>> queryParameters = request.getQueryParameters();
		if(queryParameters.containsKey("environment")) {
			environment = queryParameters.get("environment").get(0);
		}
		
		String trafficType = kTrafficTypeName;
		if(queryParameters.containsKey("trafficType")) {
			trafficType = queryParameters.get("trafficType").get(0);
		}
		
		StringWriter writer = new StringWriter();
		IOUtils.copy(request.getInputStream(), writer, StandardCharsets.UTF_8);
		String requestBody = writer.toString();
		
		JSONObject eventObj = new JSONObject(requestBody);
		List<JSONObject> events = new LinkedList<JSONObject>();
		System.out.println(eventObj.toString(2));
		events.add(eventObj);
		
		JSONArray splitEvents = new JSONArray();
		for(JSONObject e : events) {
			JSONObject dataObj = e.getJSONObject("data");
			JSONObject udoObj = dataObj.getJSONObject("udo");
			JSONObject domObj = dataObj.getJSONObject("dom");
			Map<String, Object> properties = new TreeMap<String, Object>();
			putProperties(properties, "udo", udoObj);
			putProperties(properties, "dom", domObj);
			
			properties.put("root.profile", e.getString("profile"));
			properties.put("root.account", e.getString("account"));
			properties.put("root.useragent", e.getString("useragent"));
			properties.put("root.env", e.getString("env"));
			
			String eventTypeId = udoObj.getString("tealium_event");
			String environmentName = environment;
			String trafficTypeName = trafficType;
			String key = udoObj.getString("tealium_visitor_id");
			long timestamp = e.getLong("post_time");
			Object value = 0;
			
			JSONObject event = new JSONObject();
			event.put("eventTypeId", eventTypeId);
			event.put("environmentName", environmentName);
			event.put("trafficTypeName", trafficTypeName);
			event.put("key", key);
			event.put("timestamp", timestamp);
			event.put("value", value);
			event.put("properties", properties);
			
			splitEvents.put(event);
		}
		System.out.println("===============================");
		System.out.println("===============================");

		System.out.println(splitEvents.toString(2));
		CreateEvents.doPost(splitEvents);
		
		System.out.println("finished in " + (System.currentTimeMillis() - start) + "ms");
	}

	private void putProperties(Map<String, Object> properties, String prefix, JSONObject obj) {
		for(String k : obj.keySet()) {
			if(!(obj.get(k) instanceof JSONArray)) {
				properties.put(prefix + "." + k, obj.get(k));
			} else {
				JSONArray array = obj.getJSONArray(k);
				properties.put(prefix + "." + k, array.toString());
			}
		}
	}

	static String readFile(String path, Charset encoding)
			throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
