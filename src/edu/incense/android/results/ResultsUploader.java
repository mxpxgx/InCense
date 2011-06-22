package edu.incense.android.results;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.incense.android.R;
import edu.incense.android.comm.Uploader;

import android.content.Context;
import android.util.Log;

public class ResultsUploader {
    private FileQueue fileQueue;
    private JsonResults jsonResults;
    private File queueFile;
    private Context context;

    public ResultsUploader(Context context) {
        this.context = context;
        queueFile = new File(context.getResources().getString(
                R.string.filequeue_filename));
        jsonResults = new JsonResults();

        if (queueFile.exists())
            fileQueue = loadQueue();
        if (fileQueue == null) {
            fileQueue = new FileQueue();
            int maxFiles = Integer.parseInt(context.getResources().getString(
                    R.string.filequeue_max_files));
            fileQueue.setMaxFiles(maxFiles);
            Queue<ResultFile> queue = new LinkedList<ResultFile>();
            fileQueue.setFileQueue(queue);
            saveQueue();
        }
    }

    private FileQueue loadQueue() {
        return jsonResults.toFileQueue(queueFile);
    }

    private void saveQueue() {
        jsonResults.toJson(queueFile, fileQueue);
    }

    public void offerFile(ResultFile resultFile) {
        fileQueue.offer(resultFile);
        saveQueue();
    }

    public void deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()){
            try{
                file.delete();
            } catch(Exception e){
                Log.e(getClass().getName(), "Deleting file" , e);
            }
        }
    }

    /***
     * sendFiles() tries to send everything in the the file queue.
     * 
     * @return int - number of files uploaded.
     */
    public int sendFiles() {
        Uploader uploader = new Uploader(context);
        ResultFile file;
        boolean sent = true;
        int count = 0;
        while (!fileQueue.isEmpty() && sent) {
            file = fileQueue.peek();
            sent = false;
            switch (file.getFileType()) {
            case DATA:
                sent = uploader.postSinkData(file.getFileName());
                break;
            case AUDIO:
                sent = uploader.postAudioData(file.getFileName());
                break;
            case SURVEY:
                sent = uploader.postSurveyData(file.getFileName());
                break;
            }
            if (sent) {
                file = fileQueue.poll();
                deleteFile(file.getFileName());
                count++;
            }
        }
        if (count > 0) {
            saveQueue();
        }
        return count;
    }

    public List<ResultFile> getQueueList() {
        List<ResultFile> list = new ArrayList<ResultFile>(
                fileQueue.getFileQueue());
        return Collections.unmodifiableList(list);
    }

}
