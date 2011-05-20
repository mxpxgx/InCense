package edu.incense.datatask.data;

public class AudioData extends Data {
    private String filePath;

    public AudioData() {
        super(DataType.AUDIO);
        this.setFilePath(generateFilePath());
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    private String generateFilePath() {
        String fileName = "/sdcard/audio-" + getTimestamp() + ".3gp";
        return fileName;
    }
}
