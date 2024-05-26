package com.samsung.finalproject;

import org.jetbrains.annotations.Nullable;

public class PlaceItem {
    String city;
    String time;
    double longitude;
    double latitude;
    long id;

    public PlaceItem(String city, String time, double latitude, double longitude, @Nullable Long id) {
        this.city = city;
        this.time = time;
        this.longitude = longitude;
        this.latitude = latitude;
        if (id != null) {
            this.id = id;
        }
    }

    public long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getTime() {
        return time;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}