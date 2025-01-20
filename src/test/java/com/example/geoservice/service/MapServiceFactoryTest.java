package com.example.geoservice.service;

import com.example.geoservice.config.MapServiceProperties;
import com.example.geoservice.service.impl.BaiduMapService;
import com.example.geoservice.service.impl.GoogleMapService;
import com.example.geoservice.service.impl.TencentMapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MapServiceFactoryTest {

    @Mock
    private MapServiceProperties properties;
    @Mock
    private GoogleMapService googleMapService;
    @Mock
    private TencentMapService tencentMapService;
    @Mock
    private BaiduMapService baiduMapService;

    private MapServiceFactory mapServiceFactory;

    @BeforeEach
    void setUp() {
        mapServiceFactory = new MapServiceFactory(properties, googleMapService, tencentMapService, baiduMapService);
    }

    @Test
    void shouldReturnGoogleMapService() {
        when(properties.getProvider()).thenReturn("google");
        assertSame(googleMapService, mapServiceFactory.getMapService());
    }

    @Test
    void shouldReturnTencentMapService() {
        when(properties.getProvider()).thenReturn("tencent");
        assertSame(tencentMapService, mapServiceFactory.getMapService());
    }

    @Test
    void shouldReturnBaiduMapService() {
        when(properties.getProvider()).thenReturn("baidu");
        assertSame(baiduMapService, mapServiceFactory.getMapService());
    }

    @Test
    void shouldThrowExceptionForUnsupportedProvider() {
        when(properties.getProvider()).thenReturn("unsupported");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> mapServiceFactory.getMapService());
        assertEquals("不支持的地图服务提供商: unsupported", exception.getMessage());
    }
}