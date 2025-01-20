package com.example.geoservice.controller;

import com.example.geoservice.model.Location;
import com.example.geoservice.service.MapService;
import com.example.geoservice.service.MapServiceFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Tag(name = "地理服务接口", description = "提供地理位置相关的服务，如驾车距离计算等")
@RestController
@RequestMapping("/api/geo")
public class GeoController {
    private final MapServiceFactory mapServiceFactory;

    public GeoController(MapServiceFactory mapServiceFactory) {
        this.mapServiceFactory = mapServiceFactory;
    }

    @Operation(summary = "计算驾车距离", description = "计算两个位置坐标之间的驾车距离，返回单位为米")
    @PostMapping("/distance")
    public double calculateDrivingDistance(
            @Parameter(description = "位置坐标列表，必须包含两个位置坐标，分别表示起点和终点")
            @RequestBody List<Location> locations) throws IOException {
        if (locations.size() != 2) {
            throw new IllegalArgumentException("必须提供两个位置坐标");
        }
        MapService mapService = mapServiceFactory.getMapService();
        return mapService.calculateDrivingDistance(locations.get(0), locations.get(1));
    }
}