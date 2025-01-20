package com.example.geoservice.service.impl;

import com.example.geoservice.config.MapServiceProperties;
import com.example.geoservice.model.AddressInfo;
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

    @Override
    public AddressInfo geocodeAddress(String address) throws IOException {
        // 构建请求URL，添加必要的查询参数
        HttpUrl url = HttpUrl.parse(properties.getBaseUrl() + "/geocode/json")
                .newBuilder()
                .addQueryParameter("address", address)
                .addQueryParameter("key", properties.getApiKey())
                .build();

        // 构建并执行HTTP请求
        Request request = new Request.Builder()
                .url(url)
                .build();

        String response = executeRequest(request);
        JsonNode root = objectMapper.readTree(response);

        // 验证响应状态（Google Maps使用OK表示成功）
        String status = root.path("status").asText();
        validateResponse("OK".equals(status) ? 200 : ("ZERO_RESULTS".equals(status) ? 404 : 400), root.path("error_message").asText(""));

        // 如果没有找到地址，返回空的地址信息对象
        if (root.path("results").isEmpty()) {
            return new AddressInfo();
        }

        // 解析响应数据
        JsonNode result = root.path("results").path(0);
        JsonNode location = result.path("geometry").path("location");
        JsonNode addressComponents = result.path("address_components");

        // 构建地址信息对象
        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setLatitude(location.path("lat").asDouble());
        addressInfo.setLongitude(location.path("lng").asDouble());

        // 解析地址组件
        for (JsonNode component : addressComponents) {
            String type = component.path("types").path(0).asText();
            String value = component.path("long_name").asText();

            switch (type) {
                case "administrative_area_level_1":
                    addressInfo.setProvince(value);
                    break;
                case "locality":
                    addressInfo.setCity(value);
                    break;
                case "sublocality_level_1":
                    addressInfo.setDistrict(value);
                    break;
                case "route":
                    addressInfo.setStreet(value);
                    break;
                case "street_number":
                    addressInfo.setStreetNumber(value);
                    break;
                case "postal_code":
                    addressInfo.setPostalCode(value);
                    break;
            }
        }

        addressInfo.setFormattedAddress(result.path("formatted_address").asText());
        // Google Maps API 没有直接提供置信度，这里设置为1.0表示来自官方API
        addressInfo.setConfidence(1.0);

        return addressInfo;
    }
}