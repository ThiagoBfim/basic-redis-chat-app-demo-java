package com.redisdeveloper.basicchat.model;

public class Message {
    private String from;
    private int date;
    private String message;
    private String roomId;

    public Message(String from, int date, String message, String roomId) {
        this.from = from;
        this.date = date;
        this.message = message;
        this.roomId = roomId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
