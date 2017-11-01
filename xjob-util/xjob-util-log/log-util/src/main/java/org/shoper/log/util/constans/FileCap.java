package org.shoper.log.util.constans;

/**
 * Created by ShawnShoper on 2017/4/28.
 * 文件容量
 */
public enum FileCap {
    KB("kb", 1024), MB("kb", 1024 * KB.size), GB("gb", 1024 * MB.size), TB("tb", 1024 * GB.size);
    public String name;
    public int size;

    FileCap(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
