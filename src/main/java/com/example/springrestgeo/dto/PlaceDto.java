package com.example.springrestgeo.dto;

import com.example.springrestgeo.entity.Coordinates;

import java.io.Serializable;

public record PlaceDto(String name,
                          String userId,
                         Boolean visible,
                         Integer categoryId,
                          String description,
                          Coordinates coordinate) implements Serializable {
}
