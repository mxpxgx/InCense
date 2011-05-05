package edu.incense.datatask.sink;


import java.util.ArrayList;
import java.util.List;

import edu.incense.datatask.DataTask;
import edu.incense.datatask.Input;
import edu.incense.datatask.InputEnabledTask;
import edu.incense.datatask.data.Data;



public class DataSink extends DataTask implements InputEnabledTask {
	private String name;
	private List<Data> sink=null;
	private SinkWritter sinkWritter;

	public DataSink(SinkWritter sinkWritter){
		this.sinkWritter = sinkWritter;
		inputs = new ArrayList<Input>();
		
		clear();
		initSinkList();
		setPeriodTime(1000);
	}
	
	public void stop(){
		super.stop();
		clearOutputs();
		sinkWritter.writeSink(this);
	}
	
	protected void clearOutputs(){
		// No outputs for DataSink
		//outputs.removeAll(outputs);
		outputs = null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.UrbanMoments.DataTask.DataTask#compute()
	 * Collects the most recent data from all inputs in a "sink" (List)
	 */
	@Override
	protected void compute() {
		Data latestData=null;
		for(Input i: inputs){
			do{
				latestData= i.pullData();
				if(latestData != null){
					System.out.println("Data added to sink!");
					sink.add(latestData);
				} else {
					System.out.println("Data NOT added to sink!");
				}
			}while(latestData != null);
		}
	}
	
	private List<Data> getSink(){
		return sink;
	}
	
	public List<Data> removeSink(){
		List<Data> temp = getSink();
		initSinkList();
		return temp;
	}
	
	private void initSinkList(){
		sink = new ArrayList<Data>();
	}
	
	@Override
	public void addInput(Input i){
		super.addInput(i);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
