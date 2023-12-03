package com.example.springrestgeo.entity;

import com.example.springrestgeo.utils.Point2DSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.geolatte.geom.G2D;

import org.geolatte.geom.Point;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="place")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    //private int categoryId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description should be at most 255 characters")
    private String description;
    @Column(nullable = false)
    private Boolean visible = false;
    @CreationTimestamp
    @Column(name="date_created")
    private LocalDateTime dateCreated;
    @UpdateTimestamp
    @Column(name="date_modified")
    private LocalDateTime dateModified;
    @JsonIgnore
    @JsonSerialize(using = Point2DSerializer.class)
    private Point<G2D> coordinate;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    // constructors
    public Place(){}

    public Place(int id, String name, User user, String description, Boolean visible, LocalDateTime dateCreated, LocalDateTime dateModified, Point<G2D> coordinate, Category category) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.description = description;
        this.visible = visible;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.coordinate = coordinate;
        this.category = category;
    }

    // getters and setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Point<G2D> getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Point<G2D> coordinate) {
        this.coordinate = coordinate;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // toString method


    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", user=" + user+
                ", visible=" + visible +
                ", dateCreated=" + dateCreated +
                ", dateModified=" + dateModified +
                ", coordinate=" + coordinate +
                ", category=" + category +
                '}';
    }

    //equals


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(coordinate, place.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate);
    }





}