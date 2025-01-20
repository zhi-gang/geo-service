package com.example.geoservice.service;

import com.example.geoservice.config.MapServiceProperties;
import com.example.geoservice.service.impl.BaiduMapService;
import com.example.geoservice.service.impl.GoogleMapService;
import com.example.geoservice.service.impl.TencentMapService;
import org.springframework.stereotype.Component;

/**
 * 地图服务工厂类
 * <p>
 * 该类负责根据配置创建并管理不同的地图服务实现：
 * 1. 支持Google、腾讯和百度三种地图服务
 * 2. 根据配置文件动态选择使用的地图服务提供商
 * 3. 统一管理地图服务实例的创建和获取
 * </p>
 */
@Component
public class MapServiceFactory {
    /**
     * 地图服务配置属性
     */
    private final MapServiceProperties properties;

    /**
     * Google地图服务实例
     */
    private final GoogleMapService googleMapService;

    /**
     * 腾讯地图服务实例
     */
    private final TencentMapService tencentMapService;

    /**
     * 百度地图服务实例
     */
    private final BaiduMapService baiduMapService;

    /**
     * 构造函数
     * 通过Spring依赖注入初始化所有地图服务实例
     *
     * @param properties 地图服务配置
     * @param googleMapService Google地图服务
     * @param tencentMapService 腾讯地图服务
     * @param baiduMapService 百度地图服务
     */
    public MapServiceFactory(MapServiceProperties properties,
                           GoogleMapService googleMapService,
                           TencentMapService tencentMapService,
                           BaiduMapService baiduMapService) {
        this.properties = properties;
        this.googleMapService = googleMapService;
        this.tencentMapService = tencentMapService;
        this.baiduMapService = baiduMapService;
    }

    /**
     * 获取配置的地图服务实例
     *
     * @return 根据配置返回对应的地图服务实现
     * @throws IllegalArgumentException 当配置的地图服务提供商不支持时抛出
     */
    public MapService getMapService() {
        switch (properties.getProvider().toLowerCase()) {
            case "google":
                return googleMapService;
            case "tencent":
                return tencentMapService;
            case "baidu":
                return baiduMapService;
            default:
                throw new IllegalArgumentException("不支持的地图服务提供商: " + properties.getProvider());
        }
    }
}