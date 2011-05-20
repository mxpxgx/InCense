package edu.incense.project;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.util.Log;
import edu.incense.datatask.model.TaskType;
import edu.incense.session.JsonSession;
import edu.incense.session.Session;
import edu.incense.survey.JsonSurvey;
import edu.incense.survey.Survey;

public class JsonProject {
    public final static String TIMESTAMP = "timestamp";
    public final static String NAME = "name";
    public final static String SENSORS = "sensors";
    public final static String APPKEY = "appKey";

    private ObjectMapper mapper;

    public JsonProject() {
        mapper = new ObjectMapper(); // can reuse, share globally
    }

    public ProjectSignature getProjectSignature(File signatureFile) {
        ProjectSignature projectSignature = null;
        try {
            JsonNode root = mapper.readValue(signatureFile, JsonNode.class);
            projectSignature = new ProjectSignature();

            JsonNode attribute = root.get(TIMESTAMP);
            projectSignature.setTimestamp(attribute.getValueAsLong());

            attribute = root.get(NAME);
            projectSignature.setName(attribute.getValueAsText());

            attribute = root.get(APPKEY);
            projectSignature.setAppKey(attribute.getValueAsText());

            attribute = root.get(SENSORS);
            List<TaskType> sensors = mapper.readValue(attribute,
                    new TypeReference<List<TaskType>>() {
                    });
            projectSignature.setSensors(sensors);

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
        return projectSignature;
    }

    public void toJson(String fileName, ProjectSignature projectSignature) {
        toJson(new File(fileName), projectSignature);
    }

    public void toJson(File file, ProjectSignature projectSignature) {
        try {
            mapper.writeValue(file, projectSignature);
        } catch (JsonParseException e) {
            Log.e(getClass().getName(), "Parsing JSON file failed", e);
        } catch (JsonMappingException e) {
            Log.e(getClass().getName(), "Mapping JSON file failed", e);
        } catch (IOException e) {
            Log.e(getClass().getName(), "Reading JSON file failed", e);
        }
    }

    public final static String SESSIONSSIZE = "sessionsSize";
    public final static String SURVEYSSIZE = "surveysSize";
    public final static String SESSIONS = "sessions";
    public final static String SURVEYS = "surveys";

    public Project getProject(String filename) {
        return getProject(new File(filename));
    }

    public Project getProject(File projectFile) {
        Project project = null;
        try {
            JsonNode root = mapper.readValue(projectFile, JsonNode.class);
            project = new Project();

            JsonNode attribute = root.get(SESSIONSSIZE);
            project.setSessionsSize(attribute.getValueAsInt());

            attribute = root.get(SESSIONSSIZE);
            project.setSurveysSize(attribute.getValueAsInt());

            attribute = root.get(SESSIONS);
            Map<String, JsonNode> map = mapper.readValue(attribute,
                    new TypeReference<Map<String, JsonNode>>() {
                    });

            JsonSession jsonSession = new JsonSession(mapper);
            Map<String, Session> sessions = new HashMap<String, Session>(
                    map.size());
            for (Entry<String, JsonNode> entry : map.entrySet()) {
                sessions.put(entry.getKey(),
                        jsonSession.toSession(entry.getValue()));
            }
            project.setSessions(sessions);

            attribute = root.get(SURVEYS);
            map = mapper.readValue(attribute,
                    new TypeReference<Map<String, JsonNode>>() {
                    });

            JsonSurvey jsonSurvey = new JsonSurvey(mapper);
            Map<String, Survey> surveys = new HashMap<String, Survey>(
                    map.size());
            for (Entry<String, JsonNode> entry : map.entrySet()) {
                surveys.put(entry.getKey(),
                        jsonSurvey.toSurvey(entry.getValue()));
            }
            project.setSurveys(surveys);

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
        return project;
    }
}
