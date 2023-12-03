package com.example.springrestgeo.controller;

import com.example.springrestgeo.entity.Place;
import com.example.springrestgeo.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PlaceController {
    private PlaceService placeService;


    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/places")
    ResponseEntity<List<Place>> getAllPublicPlaces(){
        List<Place> publicPlaces = placeService.getAllPublicPlaces();
        if(publicPlaces.isEmpty()){
            System.out.println("places: " + publicPlaces);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }


        return ResponseEntity.ok(publicPlaces);
    }




    @GetMapping("/places/{category}")
    public ResponseEntity<List<Place>> getAllPublicPlacesByCategory(@PathVariable String category) {

        List<Place> publicPlacesInCategory = placeService.getAllPublicPlacesInCategory(category);

        if (publicPlacesInCategory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(publicPlacesInCategory, HttpStatus.OK);
        }
    }


    @GetMapping("/places/user")
    public ResponseEntity<List<Place>> getAllPlacesForUser(@AuthenticationPrincipal UserDetails userDetails){
        // get username of loggedin user
        String userId = userDetails.getUsername();
        List<Place> places = placeService.getALLPlacesForLoggedInUser(userId);
        return  ResponseEntity.ok(places);
    }

    @PostMapping("/places")
    public ResponseEntity<Place> createAPlace(Place place, Integer categoryId){

        Place newPlace = placeService.createPlace(place, categoryId);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/places")
    public ResponseEntity<Place> updatePlace(
            @PathVariable int placeId,
            @RequestBody Place updatedPlace

    ) {
        try {
            Place updatedPlaceResult = placeService.updatePlace(placeId, updatedPlace);
            return ResponseEntity.ok(updatedPlaceResult);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/places/{placeId}")
    public ResponseEntity<String> deletePlace(@PathVariable Integer placeId) {
        placeService.deletePlace(placeId);
        String responseMessage = "Place with ID " + placeId+ " deleted successfully.";
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

}