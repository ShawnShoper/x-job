package com.daqsoft.log.core.serialize;

import java.io.Serializable;

/**
 * Created by ShawnShoper on 2017/4/18.
 */
public class Business implements Serializable{
    //这一字段根据不同业务可能
    private String content;
    //业务模块名
    private String model;
    //请求终端
    private String via = "pc";
    //日志级别
    private String level;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Business{" +
                "content='" + content + '\'' +
                ", model='" + model + '\'' +
                ", via='" + via + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
