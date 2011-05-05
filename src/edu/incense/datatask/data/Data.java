package edu.incense.datatask.data;

public abstract class Data {//implements Comparable<Data> {
	private long timestamp;
	private DataType dataType = DataType.NULL;
	
	public Data(DataType dataType){
		setTimestamp(System.currentTimeMillis());
		setDataType(dataType);
	}
	
	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	protected void setTimestamp(long time) {
		timestamp = time;
	}

	public long getTimestamp() {
		return timestamp;
	}
	
	//public abstract int compareTo(Data data);
}