package edu.incense.android.results;

import java.util.LinkedList;
import java.util.Queue;

public class FileQueue {
    private Queue<ResultFile> fileQueue;
    private int maxFiles;

    public FileQueue() {
        setFileQueue(new LinkedList<ResultFile>());
        maxFiles = 0;
    }

    public int getMaxFiles() {
        return maxFiles;
    }

    public void setMaxFiles(int maxFiles) {
        this.maxFiles = maxFiles;
    }

    public FileQueue(int maxFiles) {
        this();
        this.maxFiles = maxFiles;
    }

    public void setFileQueue(Queue<ResultFile> fileQueue) {
        this.fileQueue = fileQueue;
    }

    public Queue<ResultFile> getFileQueue() {
        return fileQueue;
    }

    public boolean isEmpty() {
        return fileQueue.isEmpty();
    }

    public ResultFile peek() {
        return fileQueue.peek();
    }

    public ResultFile poll() {
        return fileQueue.poll();
    }

    public boolean offer(ResultFile resultFile) {
        if (maxFiles > 0 && fileQueue.size() >= maxFiles) {
            poll();
        }
        return fileQueue.offer(resultFile);
    }
}
