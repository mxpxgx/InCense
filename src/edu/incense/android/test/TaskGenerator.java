/**
 * 
 */
package edu.incense.android.test;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import edu.incense.android.datatask.model.Task;
import edu.incense.android.datatask.model.TaskType;

/**
 * @author mxpxgx
 *
 */
public class TaskGenerator {
    
    public static Task createTask(ObjectMapper mapper, String name, TaskType type, int sampleFrequency){
        Task task = new Task();
        task.setName(name);
        task.setTaskType(type);
        task.setSampleFrequency(sampleFrequency); 
        return task;
    }
    
    public static Task createAudioSensor(ObjectMapper mapper, int sampleFrequency, long duration){
        Task task = TaskGenerator.createTask(mapper, "AudioSensor", TaskType.AudioSensor, sampleFrequency);
        JsonNode extrasNode = mapper.createObjectNode();
        ((ObjectNode) extrasNode).put("duration", duration);
        task.setJsonNode(extrasNode);
        return task;
    }


}
