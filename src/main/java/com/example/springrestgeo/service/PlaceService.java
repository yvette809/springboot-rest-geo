package com.example.springrestgeo.service;


import com.example.springrestgeo.entity.Category;
import com.example.springrestgeo.entity.Place;
import com.example.springrestgeo.entity.User;
import com.example.springrestgeo.exceptions.ResourceNotFoundException;
import com.example.springrestgeo.repository.CategoryRepository;
import com.example.springrestgeo.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public PlaceService(PlaceRepository placeRepository, CategoryRepository categoryRepository) {
        this.placeRepository = placeRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Place> getAllPublicPlacesByFilter() {
        return placeRepository.findAll().stream()
                .filter(Place::getVisible)
                .collect(Collectors.toList());
    }

    public List<Place> getAllPublicPlaces() {
        return placeRepository.findAllByVisible(true);
    }

    public List<Place> getAllPublicPlacesInCategory(String categoryName) {
        return placeRepository.findAll()
                .stream()
                .filter(place -> {
                    Category category = place.getCategory();
                    return category != null && category.getName().equals(categoryName) && place.getVisible();
                })
                .collect(Collectors.toList());
    }

    public List<Place> getALLPlacesForLoggedInUser(String userId) {
        return placeRepository.findAllByUserUserId(userId);
    }

    public Place createPlace(Place place, Integer categoryId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.isAuthenticated()) {
            throw new RuntimeException("User must be logged in to create a place");
        }
        User currentUser = (User) auth.getPrincipal();

        // create a new place entity
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId, categoryId));
        Place newPlace = new Place();
        newPlace.setName(place.getName());
        newPlace.setDescription(place.getDescription());
        newPlace.setVisible(place.getVisible());
        newPlace.setUser(currentUser);
        newPlace.setCategory(category);
        newPlace.setDateCreated(LocalDateTime.now()); // Set the current date/time

        // Assuming placeRequest contains the coordinates
        newPlace.setCoordinate(place.getCoordinate());
        return placeRepository.save(newPlace);
    }

    public Place updatePlace(Integer placeId, Place place) {
        // Check if user is logged in
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new RuntimeException("User must be logged in to update a place");
        }

        // Check if place exists and belongs to the logged-in user
        Place existingPlace = placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found with ID: " + placeId, placeId));

        if (!existingPlace.getUser().getUserId().equals(getCurrentUserId())) {
            throw new RuntimeException("Place does not belong to loggedInUser");
        }

        // Update place details
        existingPlace.setName(place.getName());
        existingPlace.setDescription(place.getDescription());
        existingPlace.setCategory(place.getCategory());

        // Save updated place to the database
        return placeRepository.save(existingPlace);
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails currentUser = (UserDetails) authentication.getPrincipal();
            return currentUser.getUsername();
        } else {
            return null;
        }
    }

    public void deletePlace(Integer id) {
        // user must be logged in or be an admin
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new RuntimeException("User must be logged in to delete a place");
        }

        // check if place was created by loggedIn user
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found with ID: " + id, id));

        if (!place.getUser().getUserId().equals(getCurrentUserId())) {
            throw new RuntimeException("Place does not belong to loggedInUser");
        }

        placeRepository.delete(place);
        System.out.println("Place with id:" + id + " deleted");
    }
}