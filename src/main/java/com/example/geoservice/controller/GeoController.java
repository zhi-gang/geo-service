package com.example.geoservice.controller;

import com.example.geoservice.model.AddressInfo;
import com.example.geoservice.model.Location;
import com.example.geoservice.service.MapService;
import com.example.geoservice.service.MapServiceFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "解析地址", description = "根据输入的地址字符串，返回详细的地址信息，包括经纬度坐标")
    @GetMapping("/geocode")
    public List<AddressInfo> geocodeAddress(
            @Parameter(description = "需要解析的地址字符串")
            @RequestParam String address) throws IOException {
        MapService mapService = mapServiceFactory.getMapService();
        return mapService.geocodeAddress(address);
    }
}