package com.example.springrestgeo.entity;


import com.example.springrestgeo.validation.EmojiSymbol;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "name is required")
    @Size(max = 50)
    private String name;
    @EmojiSymbol
    @NotBlank(message = "symbol is required")
    private String symbol;
    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description should be at most 255 characters")
    private String description;

    // one to many
   @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    List<Place> places = new ArrayList<>();


//constructor

    public Category(){}
    public Category(String name, String symbol, String description) {
        this.name = name;
        this.symbol = symbol;
        this.description = description;
    }

    // getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public void addPlace(Place place){
        places.add(place);
        System.out.println("place added");
    }
    public void removePlace(Place place){
        places.remove(place);
        place.setCategory(null);
    }

    // toString method

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


}