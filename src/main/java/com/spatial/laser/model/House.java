package com.spatial.laser.model;

import java.io.Serializable;

public class House implements Serializable {

    private String address;
    private String city;
    private String state;
    private String placekey;

    public House() {
    }

    public House(String address, String city, String state, String placekey) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.placekey = placekey;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPlacekey() {
        return placekey;
    }
}