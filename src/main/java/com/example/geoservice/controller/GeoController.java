package com.example.geoservice.controller;

import com.example.geoservice.model.Location;
import com.example.geoservice.service.MapService;
import com.example.geoservice.service.MapServiceFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/geo")
public class GeoController {
    private final MapServiceFactory mapServiceFactory;

    public GeoController(MapServiceFactory mapServiceFactory) {
        this.mapServiceFactory = mapServiceFactory;
    }

    @PostMapping("/distance")
    public double calculateDrivingDistance(@RequestBody List<Location> locations) throws IOException {
        if (locations.size() != 2) {
            throw new IllegalArgumentException("必须提供两个位置坐标");
        }
        MapService mapService = mapServiceFactory.getMapService();
        return mapService.calculateDrivingDistance(locations.get(0), locations.get(1));
    }
}