package com.example.springrestgeo.service;


import com.example.springrestgeo.dto.PlaceDto;
import com.example.springrestgeo.entity.Category;
import com.example.springrestgeo.entity.Coordinates;
import com.example.springrestgeo.entity.Place;
import com.example.springrestgeo.exceptions.ResourceNotFoundException;
import com.example.springrestgeo.repository.CategoryRepository;
import com.example.springrestgeo.repository.PlaceRepository;
import jakarta.transaction.Transactional;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.Point;
import org.geolatte.geom.builder.DSL;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.geolatte.geom.builder.DSL.g;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

@Service
public class PlaceService {
     PlaceRepository placeRepository;
     CategoryRepository categoryRepository;


    // Define a logger for the PlaceService class
    private static final Logger logger = LoggerFactory.getLogger(PlaceService.class);


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
        return placeRepository.findAllByUserId(userId);
    }


    public Place createPlace(PlaceDto place) {
        String userName = getCurrentUserId();

        if (userName== null) {
            throw new RuntimeException("User must be logged in to create a place");
        }

        // Create a new place entity
      var category = categoryRepository.findById(place.categoryId());
      logger.info("Category ID from PlaceDto" + place.categoryId());
        logger.info("category:" + category);

        if(category.isEmpty()){
            throw new IllegalArgumentException("No such category exists");
        }

        var geo = Geometries.mkPoint(new G2D(place.coordinate().getLat(), place.coordinate().getLng()), WGS84);
        Place newPlace = new Place();
        newPlace.setName(place.name());
        newPlace.setDescription(place.description());
        newPlace.setVisible(place.visible());
        newPlace.setUserId(userName);
        newPlace.setCategory(category.get());
        newPlace.setCoordinate(geo);

        return placeRepository.save(newPlace);
    }


    public Place updatePlace(Integer placeId, PlaceDto place) {
        // Check if user is logged in
        String currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User must be logged in to update a place");
        }

        // Check if place exists and belongs to the logged-in user
        Place existingPlace = placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found with ID: " + placeId));

        if (!existingPlace.getUserId().equals(getCurrentUserId())) {
            throw new RuntimeException("Place does not belong to loggedInUser");
        }
        Optional<Category> category = categoryRepository.findById(place.categoryId());

        // Update place details
        existingPlace.setCategory(category .orElseThrow(()-> new ResourceNotFoundException("category not found")));
        existingPlace.setName(place.name());
        existingPlace.setDescription(place.description());
        // Save updated place to the database
        return placeRepository.save(existingPlace);
    }

    private String getCurrentUserId() {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            UserDetails currentUser = (UserDetails) auth.getPrincipal();
            return currentUser.getUsername();
        } else {
            return null;
        }
    }

    public void deletePlace(Integer id) {
        // user must be logged in or be an admin
        // Check if user is logged in
        String currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User must be logged in to update a place");
        }


        // check if place was created by loggedIn user
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found with ID: " + id));

        if (!place.getUserId().equals(getCurrentUserId())) {
            throw new RuntimeException("Place does not belong to loggedInUser");
        }

        placeRepository.delete(place);
        System.out.println("Place with id:" + id + " deleted");
    }

    // find location within a distance
    public List<Place> findAround(double lat, double lon, double distance) {
        Point<G2D> location = DSL.point(WGS84, g(lon, lat));
        return placeRepository.filterOnDistance(location, distance);
    }

    public List<Place> findWithinPolygon(Coordinates[] coordinates) {
        if (coordinates == null || coordinates.length != 4) {
            throw new IllegalArgumentException("For filtering within an area, four coordinate points are required.");
        }

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] polygonCoordinates = new Coordinate[coordinates.length + 1];
        for (int i = 0; i < coordinates.length; i++) {
            polygonCoordinates[i] = new Coordinate(coordinates[i].getLng(), coordinates[i].getLat());
        }
        polygonCoordinates[coordinates.length] = polygonCoordinates[0];
        Polygon polygon = geometryFactory.createPolygon(polygonCoordinates);

        polygon.setSRID(4326);

        return placeRepository.filterWithinPolygon(String.valueOf(polygon));
    }
}
