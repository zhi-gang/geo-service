package com.example.geoservice.service.impl;

import com.example.geoservice.config.MapServiceProperties;
import com.example.geoservice.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaiduMapServiceTest {

    @Mock
    private MapServiceProperties properties;

    private BaiduMapService baiduMapService;
    private MapServiceProperties.BaiduMapProperties baiduProperties;

    @BeforeEach
    void setUp() {
        baiduProperties = new MapServiceProperties.BaiduMapProperties();
        baiduProperties.setApiKey("test-api-key");
        baiduProperties.setBaseUrl("https://api.map.baidu.com");
        when(properties.getBaidu()).thenReturn(baiduProperties);

        baiduMapService = spy(new BaiduMapService(properties));
    }

    @Test
    void shouldCalculateDrivingDistance() throws IOException {
        // Given
        Location origin = new Location();
        origin.setLat(39.915);
        origin.setLng(116.404);
        Location destination = new Location();
        destination.setLat(31.230);
        destination.setLng(121.473);
        String mockResponse = "{\"status\": 0, \"message\": \"ok\", \"result\": {\"routes\": [{\"distance\": 1234.56}]}}";

        doReturn(mockResponse).when(baiduMapService).executeRequest(any());

        // When
        double distance = baiduMapService.calculateDrivingDistance(origin, destination);

        // Then
        assertEquals(1234.56, distance, 0.01);
    }

    @Test
    void shouldThrowExceptionWhenApiReturnsError() throws IOException {
        // Given
        Location origin = new Location();
        origin.setLat(39.915);
        origin.setLng(116.404);
        Location destination = new Location();
        destination.setLat(31.230);
        destination.setLng(121.473);
        String mockResponse = "{\"status\": 1, \"message\": \"error message\"}";

        doReturn(mockResponse).when(baiduMapService).executeRequest(any());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> baiduMapService.calculateDrivingDistance(origin, destination));
        assertEquals("API调用失败: error message", exception.getMessage());
    }
}