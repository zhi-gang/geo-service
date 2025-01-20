package com.example.geoservice.service.impl;

import com.example.geoservice.config.MapServiceProperties;
import com.example.geoservice.model.Location;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 腾讯地图服务实现类
 * <p>
 * 该类通过调用腾讯地图API实现驾车距离计算功能：
 * 1. 使用腾讯地图Direction API
 * 2. 支持签名验证
 * 3. 解析路线距离信息
 * </p>
 */
@Service
public class TencentMapService extends AbstractMapService {
    /**
     * 腾讯地图API配置属性
     */
    private final MapServiceProperties.TencentMapProperties properties;

    /**
     * 构造函数
     *
     * @param properties 地图服务配置
     */
    public TencentMapService(MapServiceProperties properties) {
        this.properties = properties.getTencent();
    }

    /**
     * 计算两个位置之间的驾车距离
     * <p>
     * 调用腾讯地图Direction API计算驾车路线，并返回最短路线的距离
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
        HttpUrl.Builder urlBuilder = HttpUrl.parse(properties.getBaseUrl() + "/direction/v1/driving")
                .newBuilder()
                .addQueryParameter("from", origin.getLatitude() + "," + origin.getLongitude())
                .addQueryParameter("to", destination.getLatitude() + "," + destination.getLongitude())
                .addQueryParameter("key", properties.getApiKey())
                .addQueryParameter("output", "json");

        // 计算并添加签名
        String sig = calculateSignature(urlBuilder.build());
        urlBuilder.addQueryParameter("sig", sig);

        // 构建并执行HTTP请求
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        String response = executeRequest(request);
        JsonNode root = objectMapper.readTree(response);

        // 验证响应状态
        int status = root.path("status").asInt();
        validateResponse(status == 0 ? 200 : 400, root.path("message").asText());

        // 解析并返回路线距离
        return root.path("result")
                .path("routes")
                .path(0)
                .path("distance")
                .asDouble();
    }

    /**
     * 计算请求签名
     * <p>
     * 使用MD5算法对请求URL和密钥进行签名
     * </p>
     *
     * @param url 完整的请求URL
     * @return MD5签名字符串
     */
    private String calculateSignature(HttpUrl url) {
        try {
            // 拼接URL和密钥
            String plaintext = url.toString() + properties.getSecretKey();
            // 计算MD5哈希
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(plaintext.getBytes());
            // 转换为十六进制字符串
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5算法不可用", e);
        }
    }
}