package edu.incense.android.results;

import java.io.File;
import java.util.Date;

import edu.incense.android.R;

import android.content.Context;

public class ResultFile {
    private String fileName;
    private long timestamp;
    private FileType fileType;

    public ResultFile() {
    }

    private ResultFile(long timestamp, String fileName, FileType fileType) {
        setTimestamp(timestamp);
        setFileName(fileName);
        setFileType(fileType);
    }

    public static ResultFile createInstance(Context context, FileType fileType) {
        long timestamp = System.currentTimeMillis();

        String parent = "./";
        String child = "data";
        String extension = ".json";

        switch (fileType) {
        case DATA:
            parent = context.getResources().getString(
                    R.string.results_data_parent);
            child = context.getResources().getString(
                    R.string.results_data_child);
            extension = context.getResources().getString(
                    R.string.results_data_extension);
            break;
        case AUDIO:
            parent = context.getResources().getString(
                    R.string.results_audio_parent);
            child = context.getResources().getString(
                    R.string.results_audio_child);
            extension = context.getResources().getString(
                    R.string.results_audio_extension);
            break;
        case SURVEY:
            parent = context.getResources().getString(
                    R.string.results_survey_parent);
            child = context.getResources().getString(
                    R.string.results_survey_child);
            extension = context.getResources().getString(
                    R.string.results_survey_extension);
            break;
        }
        File file = new File(parent, child + timestamp + extension);
        return new ResultFile(timestamp, file.getAbsolutePath(), fileType);
    }

    public static ResultFile createDataInstance(Context context,
            String extraName) {
        long timestamp = System.currentTimeMillis();
        String parent = "./";
        String child = "data";
        String extension = ".json";
        parent = context.getResources().getString(R.string.results_data_parent);
        child = context.getResources().getString(R.string.results_data_child);
        extension = context.getResources().getString(
                R.string.results_data_extension);
        File file = new File(parent, child + extraName + timestamp + extension);
        return new ResultFile(timestamp, file.getAbsolutePath(), FileType.DATA);
    }

    public String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    private void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public FileType getFileType() {
        return fileType;
    }

    private void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    /*
     * private String getFileNameOnly(){ int index = fileName.lastIndexOf("/");
     * if(index < 0) return fileName; return fileName.substring(index); }
     */

    @Override
    public String toString() {
        return "[" + fileType + "] " + (new Date(getTimestamp()));
    }

}
