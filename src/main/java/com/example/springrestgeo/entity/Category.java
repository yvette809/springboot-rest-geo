package com.example.springrestgeo.entity;

import com.example.springrestplaces.validation.EmojiSymbol;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

import java.io.IOException;
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
    @Transient
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Place> places;


//constructor

    public Category(){}
    public Category(String name, String symbol, String description) {
        this.name = name;
        this.symbol = symbol;
        this.description = description;
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

    @Entity
    @Table(name = "authorities")
    public static class Authority {

        @Id
        @Column(name = "user_id", nullable = false)
        private String userId;

        @Column(name = "role", nullable = false)
        private String authority;

        @OneToOne
        @JoinColumn(name = "user_id" ,insertable=false, updatable=false)
        private User user;

        // Constructors, getters, and setters

        // Constructors
        public Authority() {
            // Default constructor
        }

        public Authority(String userId, String authority) {
            this.userId = userId;
            this.authority = authority;
        }

        // Getters and setters


        public String getUsername() {
            return userId;
        }

        public void setUsername(String username) {
            this.userId = userId;
        }

        public String getAuthority() {
            return authority;
        }

        public void setAuthority(String authority) {
            this.authority = authority;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        //toString

        @Override
        public String toString() {
            return "Authority{" +
                    ", userId='" + userId + '\'' +
                    ", authority='" + authority + '\'' +
                    ", user=" + user +
                    '}';
        }

        public static class Point2DSerializer extends JsonSerializer<Point<G2D>> {
            @Override
            public void serialize(Point<G2D> value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
                //        gen.writeStartArray();
                //        gen.writeNumber(value.getPosition().getCoordinate(1));
                //        gen.writeNumber(value.getPosition().getCoordinate(0));
                //        gen.writeEndArray();
                gen.writeStartObject();
                gen.writeNumberField("lat", value.getPosition().getCoordinate(1));
                gen.writeNumberField("lng", value.getPosition().getCoordinate(0));
                gen.writeEndObject();

            }
        }
    }
}