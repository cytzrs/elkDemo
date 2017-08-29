package com.elastic.stack.demo.elkDemo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by liyang on 2017/8/24.
 */
@Document(indexName = "logstash-2017.08.24", type = "log")
public class LogRecord {

    @Id
    private String id;

    private String  message;

    private String source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return "LogRecord{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
