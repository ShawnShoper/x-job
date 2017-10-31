package com.daqsoft.log.util.constans;

public enum LogLevel {
    Debug("DEBUG", 0), Info("INFO", 1), Warn("Warn", 2), Error("ERROR", 3), Fatal("FATAL", 4);

    LogLevel(String name, int value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private int value;

    private String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    private int getValue() {
        return value;
    }

    private void setValue(short value) {
        this.value = value;
    }

    /**
     * 日志级别比较,0等于,-1小于,1大于
     * @param logLevel
     * @return
     */
    public int compare(LogLevel logLevel) {
        if (this.value > logLevel.value) return 1;
        else if (this == logLevel) return 0;
        else return -1;
    }
}
