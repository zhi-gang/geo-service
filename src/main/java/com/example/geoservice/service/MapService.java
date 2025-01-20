package com.example.geoservice.service;

import com.example.geoservice.model.AddressInfo;
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

    /**
     * 解析地址字符串，获取详细的地址信息
     *
     * @param address 需要解析的地址字符串
     * @return 包含详细信息的地址对象
     * @throws IOException 当网络请求失败时抛出
     */
    AddressInfo geocodeAddress(String address) throws IOException;
}