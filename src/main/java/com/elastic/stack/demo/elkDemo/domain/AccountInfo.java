package com.elastic.stack.demo.elkDemo.domain;

import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by liyang on 2017/8/22.
 */
@Document(indexName = "filebeat-2017.08.22",type = "doc", shards = 1,replicas = 0, refreshInterval = "-1")
public class AccountInfo {

    private String id;

    private String beat;

    private String message;

    private String source;

    public AccountInfo(String beat, String message, String source) {
        this.beat = beat;
        this.message = message;
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeat() {
        return beat;
    }

    public void setBeat(String beat) {
        this.beat = beat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "id='" + id + '\'' +
                ", beat='" + beat + '\'' +
                ", message='" + message + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
