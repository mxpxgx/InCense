package edu.incense.datatask.filter;


import java.util.ArrayList;

import android.util.Log;

import edu.incense.datatask.DataTask;
import edu.incense.datatask.Input;
import edu.incense.datatask.InputEnabledTask;
import edu.incense.datatask.Output;
import edu.incense.datatask.OutputEnabledTask;
import edu.incense.datatask.data.Data;


public abstract class DataFilter extends DataTask implements OutputEnabledTask, InputEnabledTask {
	private String filterName = "Unavailable";
	
	public DataFilter(){
		super();
		inputs = new ArrayList<Input>();
		outputs = new ArrayList<Output>();
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public String getFilterName() {
		return filterName;
	}

	@Override
	protected void compute() {
		Data tempData;
		for(Input i: inputs){
			Log.i(getClass().getName(), "Asking for new data");
			tempData = i.pullData();
			if(tempData != null){
				computeSingleData(tempData);
				Log.i(getClass().getName(), "GOOD");
			} else {
				Log.i(getClass().getName(), "BAD");
			}
		}
	}
	
	protected abstract void computeSingleData(Data data);
	
	@Override
	public void addOutput(Output o){
		super.addOutput(o);
	}

	
	@Override
	public void addInput(Input i){
		super.addInput(i);
	}

}
