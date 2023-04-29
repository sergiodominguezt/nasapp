package com.example.nasapp;

public class Data {

    private String id;
    private String name;
    private int userId;


    public Data(String id, String name, int userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}


