package edu.incense.android.session;

import java.util.List;

import edu.incense.android.datatask.model.Task;
import edu.incense.android.datatask.model.TaskRelation;

public class Session {
    private List<Task> tasks;
    private List<TaskRelation> relations;
    private long duration; // Time length of recording session
    private boolean autoTriggered; // automatically triggered
    private long startDate; // The date when this session will be executed for
                            // the first time
    private long endDate; // If it's repeating, the date it will stop repeating.
    private RepeatType repeatType; // Type of repeating units (eg. Hours)
    private int repeatUnits; // The repeating units length (eg. 8)
                             // If repeatType = hours and repeatUnits = 8, then
                             // this session will repeat every 8 hours

    public enum RepeatType {
        NOT_REPEATABLE, MINUTES, HOURS, DAYS, WEEKS, MONTHS
    };

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

    /**
     * @param autoTriggered
     *            the autoTriggered to set
     */
    public void setAutoTriggered(boolean autoTriggered) {
        this.autoTriggered = autoTriggered;
    }

    /**
     * @return the autoTriggered
     */
    public boolean isAutoTriggered() {
        return autoTriggered;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the startDate
     */
    public long getStartDate() {
        return startDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the endDate
     */
    public long getEndDate() {
        return endDate;
    }

    /**
     * @param repeatType the repeatType to set
     */
    public void setRepeatType(RepeatType repeatType) {
        this.repeatType = repeatType;
    }

    /**
     * @return the repeatType
     */
    public RepeatType getRepeatType() {
        return repeatType;
    }

    /**
     * @param repeatUnits the repeatUnits to set
     */
    public void setRepeatUnits(int repeatUnits) {
        this.repeatUnits = repeatUnits;
    }

    /**
     * @return the repeatUnits
     */
    public int getRepeatUnits() {
        return repeatUnits;
    }

}
