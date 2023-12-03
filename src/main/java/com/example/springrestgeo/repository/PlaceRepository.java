package com.example.springrestgeo.repository;


import com.example.springrestgeo.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place,Integer> {
    List<Place> findAllByVisible(Boolean  visible);
    List<Place>findAllByVisibleAndCategory_Name(Boolean visible, String categoryName);
    List<Place> findAllByUserUserId(String userId);
}
