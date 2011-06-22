package edu.incense.android.session;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import edu.incense.android.datatask.model.Task;
import edu.incense.android.datatask.model.TaskRelation;

import android.util.Log;

public class JsonSession {
    public final static String TASKS = "tasks";
    public final static String RELATIONS = "relations";
    public final static String DURATION = "duration";

    private ObjectMapper mapper;

    public JsonSession() {
        mapper = new ObjectMapper(); // can reuse, share globally
    }

    public JsonSession(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Session toSession(JsonNode root) {
        Session session = null;
        try {
            session = new Session();
            JsonNode attribute = root.get(DURATION);
            long duration = attribute.getValueAsLong();
            session.setDuration(duration);

            attribute = root.get(TASKS);
            List<Task> tasks = mapper.readValue(attribute,
                    new TypeReference<List<Task>>() {
                    });
            if (tasks != null)
                session.setTasks(tasks);
            else {
                Log.e(getClass().getName(),
                        "Tasks JSON node was empty/null or doesn't exist");
            }
            attribute = root.get(RELATIONS);
            List<TaskRelation> relations = mapper.readValue(attribute,
                    new TypeReference<List<TaskRelation>>() {
                    });
            if (relations != null)
                session.setRelations(relations);
            else {
                Log.e(getClass().getName(),
                        "TaskRelation JSON node was empty/null or doesn't exist");
            }

        } catch (JsonParseException e) {
            Log.e(getClass().getName(), "Parsing JSON file failed", e);
            return null;
        } catch (JsonMappingException e) {
            Log.e(getClass().getName(), "Mapping JSON file failed", e);
            return null;
        } catch (IOException e) {
            Log.e(getClass().getName(), "Reading JSON file failed", e);
            return null;
        }
        return session;
    }

}
