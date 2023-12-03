package com.example.springrestgeo.entity;

package com.example.springrestplaces.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="users")
public class User {
    @Id
    @Column(nullable = false ,unique = true)
    private String userId;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean enabled;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Category.Authority authority;
    @OneToMany(mappedBy = "user")
    private List<Place> places;

    // Constructors
    public User(){}

    public User(String userId, String password, boolean enabled, Category.Authority authority) {
        this.userId= userId;
        this.password = password;
        this.enabled = enabled;
        this.authority = authority;
    }

    // getters and setters



    public String getUserId() {
        return userId;
    }

    public void setUsername(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public Category.Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Category.Authority authority) {
        this.authority = authority;
    }

// toString method


    @Override
    public String toString() {
        return "User{" +

                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", authority=" + authority +
                ", places=" + places +
                '}';
    }
}