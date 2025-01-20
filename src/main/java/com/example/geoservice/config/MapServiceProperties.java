package com.example.geoservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "map")
public class MapServiceProperties {
    private String provider = "google"; // 默认使用 Google Maps
    private String language = "zh"; // 默认使用中文
    private GoogleMapProperties google = new GoogleMapProperties();
    private TencentMapProperties tencent = new TencentMapProperties();
    private BaiduMapProperties baidu = new BaiduMapProperties();

    @Data
    public static class GoogleMapProperties {
        private String apiKey;
        private String baseUrl = "https://maps.googleapis.com/maps/api";
        private String language = "zh-CN";
    }

    @Data
    public static class TencentMapProperties {
        private String apiKey;
        private String secretKey;
        private String baseUrl = "https://apis.map.qq.com/ws";
        private String language = "zh-CN";
    }

    @Data
    public static class BaiduMapProperties {
        private String apiKey;
        private String baseUrl = "https://api.map.baidu.com";
        private String language = "zh-CN";
    }
}