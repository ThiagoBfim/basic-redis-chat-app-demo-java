package com.redisdeveloper.basicchat.model;

public class Room {
    private String id;
    private String[] names;

    public Room(String id, String name) {
        this.id = id;
        this.names = new String[1];
        this.names[0] = name;
    }

    public Room(String id, String name1, String name2) {
        this.id = id;
        this.names = new String[2];
        this.names[0] = name1;
        this.names[1] = name2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }
}
