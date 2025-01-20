package com.example.geoservice.service;

import com.example.geoservice.model.AddressInfo;
import com.example.geoservice.model.Location;
import java.io.IOException;
import java.util.List;

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
     * 解析地址字符串，获取所有可能匹配的地址信息列表
     *
     * @param address 需要解析的地址字符串
     * @return 包含所有可能匹配地址信息的列表
     * @throws IOException 当网络请求失败时抛出
     */
    List<AddressInfo> geocodeAddress(String address) throws IOException;
}