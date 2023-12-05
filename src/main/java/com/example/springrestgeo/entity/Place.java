package com.example.springrestgeo.entity;

import com.example.springrestgeo.utils.Point2DSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.geolatte.geom.G2D;

import org.geolatte.geom.Point;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

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
    @Column(name = "user_id")
    private String userId;

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


    @JsonSerialize(using = Point2DSerializer.class)
    private Point<G2D> coordinate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="category_id")
    private Category category;

    // constructors
    public Place(){}

    public Place(String name, String userId, String description, Boolean visible, LocalDateTime dateCreated, LocalDateTime dateModified, Point<G2D> coordinate, Category category) {
        this.name = name;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
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

    public Point<G2D> getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Point<G2D> coordinate) {
        this.coordinate = coordinate;
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
                ", userId=" + userId+
                ", visible=" + visible +
                ", dateCreated=" + dateCreated +
                ", dateModified=" + dateModified +
                ", coordinate=" + coordinate +
                ", category=" + category +
                '}';
    }

    //equals








}