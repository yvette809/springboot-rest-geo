package com.example.springrestgeo.controller;

import com.example.springrestgeo.dto.PlaceDto;
import com.example.springrestgeo.entity.Coordinates;
import com.example.springrestgeo.entity.Place;
import com.example.springrestgeo.service.PlaceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/places")
public class PlaceController {
    private PlaceService placeService;


    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping()
    ResponseEntity<List<Place>> getAllPublicPlaces(){
        List<Place> publicPlaces = placeService.getAllPublicPlaces();
        if(publicPlaces.isEmpty()){
            System.out.println("places: " + publicPlaces);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }


        return ResponseEntity.ok(publicPlaces);
    }


    @GetMapping(path = "/search")
    public ResponseEntity<?> getWithin(@RequestParam Optional<Double> lat,
                                       @RequestParam Optional<Double> lon,
                                       @RequestParam Optional<Double> dist,
                                       @RequestBody(required = false) Optional<Coordinates[]> coordinates) {

        if (dist.isPresent() && lat.isPresent() && lon.isPresent()) {
            return ResponseEntity.ok().body(placeService.findAround(lat.get(), lon.get(), dist.get()));
        }
        return null;
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<Place>> getAllPublicPlacesByCategory(@PathVariable String category) {

        List<Place> publicPlacesInCategory = placeService.getAllPublicPlacesInCategory(category);

        if (publicPlacesInCategory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(publicPlacesInCategory, HttpStatus.OK);
        }
    }


    @GetMapping("/user")
    public ResponseEntity<List<Place>> getAllPlacesForUser(@AuthenticationPrincipal UserDetails userDetails){
        // get username of loggedin user
        String userId = userDetails.getUsername();
        List<Place> places = placeService.getALLPlacesForLoggedInUser(userId);
        return  ResponseEntity.ok(places);
    }

    @PostMapping()
    public ResponseEntity<Place> createAPlace(PlaceDto place){

        Place newPlace = placeService.createPlace(place);

        URI locationURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(newPlace.getId())
                .toUri();

        return ResponseEntity.created(locationURI).body(newPlace);
    }

    @PutMapping()
    public ResponseEntity<Place> updatePlace(
            @PathVariable int placeId,
            @RequestBody PlaceDto updatedPlace

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

    @DeleteMapping("/{placeId}")
    public ResponseEntity<String> deletePlace(@PathVariable Integer placeId) {
        placeService.deletePlace(placeId);
        String responseMessage = "Place with ID " + placeId+ " deleted successfully.";
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

}