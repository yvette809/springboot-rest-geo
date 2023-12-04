package com.example.springrestgeo.repository;


import com.example.springrestgeo.entity.Place;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place,Integer> {
    List<Place> findAllByVisible(Boolean  visible);
    List<Place> findAllByUserId(String userId);
    @Query(value = """
            SELECT * FROM place
            WHERE ST_Within(coordinate, ST_GeomFromText(:polygon, 4326))
                """, nativeQuery = true)
    List<Place> filterWithinPolygon(@Param("polygon") String polygon);

    @Query(value = """
            SELECT * FROM place
            WHERE ST_Distance_Sphere(coordinate, :place) < :distance
                """, nativeQuery = true)
    List<Place> filterOnDistance(@Param("place") Point<G2D> place, @Param("distance") double distance);
}

