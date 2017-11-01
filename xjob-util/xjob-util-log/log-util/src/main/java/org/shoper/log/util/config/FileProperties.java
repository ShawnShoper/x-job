package org.shoper.log.util.config;

/**
 * Created by ShawnShoper on 2017/4/21.
 */
public class FileProperties {
    public enum Rolling {
        Hour("yyyy-MM-dd-HH"), Day("yyyy-MM-dd"), Month("yyyy-MM"), Year("yyyy");
        String pattern;
        Rolling(String pattern) {
            this.pattern = pattern;
        }
        public String getPattern() {
            return pattern;
        }
        public void setPattern(String pattern) {
            this.pattern = pattern;
        }
    }

    private String fileDir;
    private String fileName;
    private Rolling rolling;
    private String fileSize;

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Rolling getRolling() {
        return rolling;
    }

    public void setRolling(Rolling rolling) {
        this.rolling = rolling;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
