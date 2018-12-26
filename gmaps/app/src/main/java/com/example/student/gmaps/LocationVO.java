package com.example.student.gmaps;

/**
 * Created by student on 2018-12-26.
 */

public class LocationVO {
    double lat;
    double lng;
    String name;

    String addr;

    public LocationVO(double lat, double lng, String name, String addr) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.addr = addr;
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

}
