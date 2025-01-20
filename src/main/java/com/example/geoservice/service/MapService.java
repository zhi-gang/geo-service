package com.example.geoservice.service;

import com.example.geoservice.model.Location;
import java.io.IOException;

public interface MapService {
    /**
     * 计算两个位置之间的驾车距离
     *
     * @param origin      起点
     * @param destination 终点
     * @return 驾车距离（米）
     */
    double calculateDrivingDistance(Location origin, Location destination) throws IOException;
}