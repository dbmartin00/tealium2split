package io.split.dbm.integrations;

import java.util.Map;

public class SplitEvent {

	public String eventTypeId;
	public String environmentName;
	public String trafficTypeName;
	public String key;
	public long timestamp;
	public Object value;
	public Map<String, Object> properties;
	public String treatment;
	
	public SplitEvent(
			String eventTypeId, 
			String environmentName, 
			String trafficTypeName, 
			String key, 
			long timestamp,
			Object value,
			Map<String, Object> properties) 
			{
		super();
		this.eventTypeId = eventTypeId;
		this.environmentName = environmentName;
		this.trafficTypeName = trafficTypeName;
		this.key = key;
		this.timestamp = timestamp;
		this.value = value;
		this.properties = properties;
	}
	
	public String getEventTypeId() {
		return eventTypeId;
	}
	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
	}
	public String getEnvironmentName() {
		return environmentName;
	}
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}
	public String getTrafficTypeName() {
		return trafficTypeName;
	}
	public void setTrafficTypeName(String trafficTypeName) {
		this.trafficTypeName = trafficTypeName;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Map<String, Object> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	@Override
	public String toString() {
		return "SplitEvent [eventTypeId=" + eventTypeId + ", environmentName=" + environmentName + ", trafficTypeName="
				+ trafficTypeName + ", key=" + key + ", timestamp=" + timestamp + ", value=" + value + ", properties="
				+ properties + "]";
	}

			
			
}
