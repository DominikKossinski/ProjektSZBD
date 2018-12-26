package com.example.ProjektSZBD.Data;

public class Hospital {

    private int id;
    private String name;
    private String address;
    private String city;

    public Hospital(int id, String name, String address, String city) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
    }

    public String toJSONString() {
        return "{\"id\":" + id + ", \"name\":\"" + name + "\", \"address\":\"" +
                address + "\", \"city\":\"" + city + "\"}";
    }
}
