package com.example.geoservice.service.impl;

import com.example.geoservice.config.MapServiceProperties;
import com.example.geoservice.model.Location;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 百度地图服务实现类
 * <p>
 * 该类通过调用百度地图API实现驾车距离计算功能：
 * 1. 使用百度地图Direction API v2
 * 2. 支持API密钥认证
 * 3. 解析路线距离信息
 * </p>
 */
@Service
public class BaiduMapService extends AbstractMapService {
    /**
     * 百度地图API配置属性
     */
    private final MapServiceProperties.BaiduMapProperties properties;

    /**
     * 构造函数
     *
     * @param properties 地图服务配置
     */
    public BaiduMapService(MapServiceProperties properties) {
        this.properties = properties.getBaidu();
    }

    /**
     * 计算两个位置之间的驾车距离
     * <p>
     * 调用百度地图Direction API计算驾车路线，并返回最短路线的距离
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
        HttpUrl url = HttpUrl.parse(properties.getBaseUrl() + "/direction/v2/driving")
                .newBuilder()
                .addQueryParameter("origin", origin.getLatitude() + "," + origin.getLongitude())
                .addQueryParameter("destination", destination.getLatitude() + "," + destination.getLongitude())
                .addQueryParameter("ak", properties.getApiKey())
                .addQueryParameter("output", "json")
                .build();

        // 构建并执行HTTP请求
        Request request = new Request.Builder()
                .url(url)
                .build();

        String response = executeRequest(request);
        JsonNode root = objectMapper.readTree(response);

        // 验证响应状态（百度地图使用0表示成功）
        int status = root.path("status").asInt();
        validateResponse(status == 0 ? 200 : 400, root.path("message").asText());

        // 解析并返回路线距离
        return root.path("result")
                .path("routes")
                .path(0)
                .path("distance")
                .asDouble();
    }
}