package edu.incense.android.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import edu.incense.android.InCenseApplication;
import edu.incense.android.datatask.DataTask;
import edu.incense.android.datatask.DataTaskFactory;
import edu.incense.android.datatask.InputEnabledTask;
import edu.incense.android.datatask.OutputEnabledTask;
import edu.incense.android.datatask.PipeBuffer;
import edu.incense.android.datatask.model.Task;
import edu.incense.android.datatask.model.TaskRelation;
import edu.incense.android.datatask.model.TaskType;
import edu.incense.android.datatask.trigger.GeneralTrigger;

public class SessionController {
    private static final String TAG = "SessionController";

    private Session session;
    private List<DataTask> tasks;
    private Context context;

    public SessionController(Context context, Session session) {
        this.context = context;
        this.session = session;
        tasks = new ArrayList<DataTask>();
    }

    public void prepareSession() {
        // TODO The usage of this HashMap could be improved (?)
        // Map<String, DataTask> taskCollection =
        // InCenseApplication.getInstance()
        // .getTaskCollection();
        Map<String, DataTask> taskCollection = ((InCenseApplication) context
                .getApplicationContext()).getTaskCollection();

        // Initializes DataTasks if necessary
        List<Task> tasks = session.getTasks();
        DataTask dataTask = null;
        for (Task t : tasks) {
            dataTask = taskCollection.get(t.getName());
            if (dataTask == null) {
                dataTask = DataTaskFactory.createDataTask(t, context);
                taskCollection.put(t.getName(), dataTask);
                Log.i(getClass().getName(), "DataTask added: " + t.getName());
            }
            this.tasks.add(dataTask);
        }

        // Establish relationships
        List<TaskRelation> relations = session.getRelations();
        if(relations != null){
            PipeBuffer pipeBuffer;
            OutputEnabledTask outputTask;
            InputEnabledTask inputTask;
            for (TaskRelation tr : relations) {
                DataTask task1 = taskCollection.get(tr.getTask1());
                //When first task is a trigger
                if(task1.getTaskType() == TaskType.Trigger){
                    GeneralTrigger trigger = (GeneralTrigger)task1;
                    if(tr.getTask2().endsWith("Survey")){
                        trigger.addSurvey(tr.getTask2());
                    } else if(tr.getTask2().endsWith("Session")){
                        trigger.addSession(tr.getTask2());
                    } else {
                        DataTask task2 = (DataTask)taskCollection.get(tr.getTask2());
                        trigger.addTask(task2);
                    }
                } 
                //When is not a trigger
                else {
                    outputTask = (OutputEnabledTask) taskCollection.get(tr.getTask1());
                    inputTask = (InputEnabledTask) taskCollection.get(tr.getTask2());
                    pipeBuffer = new PipeBuffer();
                    outputTask.addOutput(pipeBuffer);
                    inputTask.addInput(pipeBuffer);
                }
            }
        }
    }

    public void start() {
        run();
    }

    public void stop() {
        for (DataTask dt : tasks) {
            Log.i(TAG, "Stoping: " + dt.getClass().getName());
            if(dt.isRunning()){
                dt.stop();
            }
            dt.clear();
        }
        // if(thread != null){
        // thread.interrupt();
        // thread = null;
        // }
    }

    private void run() {
        for (DataTask dt : tasks) {
            Log.i(TAG, "Starting: " + dt.getClass().getName());
            dt.start();
        }
        Log.i(getClass().getName(), "Sleeping: " + session.getDuration()
                + " ms");
        try {
            Thread.sleep(session.getDuration());
        } catch (InterruptedException e) {
            Log.e(TAG, "Failed to sleep for " + session.getDuration() + " ms",
                    e);
        }
        stop();
    }

}
