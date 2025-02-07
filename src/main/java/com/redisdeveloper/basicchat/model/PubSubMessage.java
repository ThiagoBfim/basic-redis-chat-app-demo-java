package com.redisdeveloper.basicchat.model;

public class PubSubMessage<T> {
    private String type;
    private T data;

    public PubSubMessage(String type, T data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
