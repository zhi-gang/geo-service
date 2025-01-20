package com.example.geoservice.service.impl;

import com.example.geoservice.config.MapServiceProperties;
import com.example.geoservice.model.Location;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Google地图服务实现类
 * <p>
 * 该类通过调用Google Maps API实现驾车距离计算功能：
 * 1. 使用Google Maps Directions API
 * 2. 支持API密钥认证
 * 3. 解析路线距离信息
 * </p>
 */
@Service
public class GoogleMapService extends AbstractMapService {
    /**
     * Google地图API配置属性
     */
    private final MapServiceProperties.GoogleMapProperties properties;

    /**
     * 构造函数
     *
     * @param properties 地图服务配置
     */
    public GoogleMapService(MapServiceProperties properties) {
        this.properties = properties.getGoogle();
    }

    /**
     * 计算两个位置之间的驾车距离
     * <p>
     * 调用Google Maps Directions API计算驾车路线，并返回最短路线的距离
     * </p>
     *
     * @param origin      起点位置
     * @param destination 终点位置
     * @return 驾车距离，单位：米
     * @throws IOException 当网络请求失败时抛出
     */
    @Override
    public double calculateDrivingDistance(Location origin, Location destination) throws IOException {
        // 构建请求URL，添加必要的查询参数
        HttpUrl url = HttpUrl.parse(properties.getBaseUrl() + "/directions/json")
                .newBuilder()
                .addQueryParameter("origin", origin.getLatitude() + "," + origin.getLongitude())
                .addQueryParameter("destination", destination.getLatitude() + "," + destination.getLongitude())
                .addQueryParameter("key", properties.getApiKey())
                .addQueryParameter("mode", "driving")
                .build();

        // 构建并执行HTTP请求
        Request request = new Request.Builder()
                .url(url)
                .build();

        String response = executeRequest(request);
        JsonNode root = objectMapper.readTree(response);

        // 验证响应状态（Google Maps使用OK表示成功）
        String status = root.path("status").asText();
        validateResponse("OK".equals(status) ? 200 : 400, root.path("error_message").asText(""));

        // 解析并返回路线距离
        return root.path("routes")
                .path(0)
                .path("legs")
                .path(0)
                .path("distance")
                .path("value")
                .asDouble();
    }
}