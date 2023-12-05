package com.example.springrestgeo.entity;

import jakarta.annotation.Nullable;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private Double lat;
    private Double lng;

    // Constructors, getters, setters, etc.

    public Coordinates(Double lat, Double lon) {
        this.lat = lat;
        this.lng = lon;
    }

    // Other constructors, getters, setters, etc.

    // Ensure that null values are allowed for lat
    @Nullable
    public Double getLat() {
        return lat;
    }

    public void setLat(@Nullable Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

// Other methods
}
