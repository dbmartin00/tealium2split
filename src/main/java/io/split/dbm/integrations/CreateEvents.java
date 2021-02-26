package io.split.dbm.integrations;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;

public class CreateEvents {

	private String apiToken;
	
	public CreateEvents(String apiToken) {
		this.apiToken = apiToken;
	}
	
	public void
	doPost(JSONArray events) throws Exception {
	    CloseableHttpClient client = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost("https://events.split.io/api/events/bulk");
	    
		StringEntity entity = new StringEntity(events.toString());
	    httpPost.setEntity(entity);
	    httpPost.setHeader("Content-type", "application/json");
	    httpPost.setHeader("Authorization", "Bearer " + apiToken);
	 
	    CloseableHttpResponse response = client.execute(httpPost);
	    System.out.println(response.getStatusLine());
	    if(response.getStatusLine().getStatusCode() > 202) {
	    	System.out.println(events.toString());
	    }
	    client.close();
	}
    
}
