package edu.incense.session;

import java.util.List;

import edu.incense.datatask.model.Task;
import edu.incense.datatask.model.TaskRelation;

public class Session{
	private List<Task> tasks;
	private List<TaskRelation> relations;
	private long duration; //Time length
	
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	public List<TaskRelation> getRelations() {
		return relations;
	}
	public void setRelations(List<TaskRelation> relations) {
		this.relations = relations;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public long getDuration() {
		return duration;
	}
	
}
