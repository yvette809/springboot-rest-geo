package com.example.springrestgeo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority {

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
}