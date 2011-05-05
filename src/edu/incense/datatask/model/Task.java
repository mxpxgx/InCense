package edu.incense.datatask.model;

import org.codehaus.jackson.JsonNode;

public class Task{
	private TaskType taskType;
	private String name;
	private float sampleFrequency;
	private JsonNode jsonNode;

	public Task(){
		taskType = TaskType.NULL;
	}

	public JsonNode getJsonNode() {
		return jsonNode;
	}

	public void setJsonNode(JsonNode jsonNode) {
		this.jsonNode = jsonNode;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	public TaskType getTaskType() {
		return taskType;
	}
	
	public boolean getBoolean(String key, boolean defValue){
		if(jsonNode == null) return defValue;
		JsonNode attribute = jsonNode.get(key);
		if(attribute != null){
			boolean value = attribute.getValueAsBoolean();
			return value;
		}
		return defValue;
	}
	
	public int getInt(String key, int defValue){
		if(jsonNode == null) return defValue;
		JsonNode attribute = jsonNode.get(key);
		if(attribute != null){
			int value = attribute.getValueAsInt();
			return value;
		}
		return defValue;
	}
	
	public long getLong(String key, long defValue){
		if(jsonNode == null) return defValue;
		JsonNode attribute = jsonNode.get(key);
		if(attribute != null){
			long value = attribute.getValueAsLong();
			return value;
		}
		return defValue;
	}
	
	public String getString(String key, String defValue){
		if(jsonNode == null) return defValue;
		JsonNode attribute = jsonNode.get(key);
		if(attribute != null){
			String value = attribute.getValueAsText();
			return value;
		}
		return defValue;
	}

	public void setSampleFrequency(float sampleFrequency) {
		this.sampleFrequency = sampleFrequency;
	}

	public float getSampleFrequency() {
		return sampleFrequency;
	}
}