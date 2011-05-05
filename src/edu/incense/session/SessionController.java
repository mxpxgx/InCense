package edu.incense.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import edu.incense.InCenseApplication;
import edu.incense.datatask.DataTask;
import edu.incense.datatask.DataTaskFactory;
import edu.incense.datatask.InputEnabledTask;
import edu.incense.datatask.OutputEnabledTask;
import edu.incense.datatask.PipeBuffer;
import edu.incense.datatask.model.Task;
import edu.incense.datatask.model.TaskRelation;

public class SessionController implements Runnable {
	private Session session;
	private List<DataTask> tasks;
	private Context context;
	private Thread thread;
	
	public SessionController(Context context, Session session) {
		this.context = context;
		this.session = session;
		tasks = new ArrayList<DataTask>();
	}

	public void prepareSession(){
		Map<String, DataTask> taskCollection = InCenseApplication.getInstance().getTaskCollection();
		
		//Initializes DataTasks if necessary
		List<Task> tasks = session.getTasks();
		DataTask dataTask=null;
		for(Task t: tasks){
			dataTask = taskCollection.get(t.getName());
			if(dataTask == null){
				dataTask = DataTaskFactory.createDataTask(t, context);
				taskCollection.put(t.getName(), dataTask);
				Log.i(getClass().getName(), "DataTask added: "+t.getName());
			}
			this.tasks.add(dataTask);
		}
		
		//Establish relationships
		List<TaskRelation> relations = session.getRelations();
		PipeBuffer pipeBuffer;
		OutputEnabledTask outputTask;
		InputEnabledTask inputTask;
		for(TaskRelation tr: relations){
			outputTask = (OutputEnabledTask)taskCollection.get(tr.getTask1());
			inputTask = (InputEnabledTask)taskCollection.get(tr.getTask2());
			pipeBuffer = new PipeBuffer();
			outputTask.addOutput(pipeBuffer);
			inputTask.addInput(pipeBuffer);
		}
	}
	
	public void start(){
		
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop(){
		for(DataTask dt: tasks){
			Log.i(getClass().getName(), "Stoping: "+dt.getClass().getName());
			dt.stop();
			dt.clear();
		}
		//if(thread != null){
		//	thread.interrupt();
		//	thread = null;
		//}
	}

	public void run() {
		for(DataTask dt: tasks){
			Log.i(getClass().getName(), "Starting: "+dt.getClass().getName());
			dt.start();
		}
		Log.i(getClass().getName(), "Sleeping: "+session.getDuration()+" ms");
		try {
			Thread.sleep(session.getDuration());
		} catch (InterruptedException e) {
			Log.e(getClass().getName(), "Failed to sleep for "+session.getDuration()+" ms",e);
		}
		stop();
	}
	
}
