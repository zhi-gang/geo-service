package com.example.geoservice.service.impl;

import com.example.geoservice.service.MapService;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * 地图服务抽象基类
 * <p>
 * 该类提供了地图服务的基础功能实现：
 * 1. HTTP客户端管理
 * 2. JSON序列化工具
 * 3. HTTP请求执行
 * 4. 响应验证
 * </p>
 */
public abstract class AbstractMapService implements MapService {
    /**
     * HTTP客户端实例
     * 用于执行HTTP请求，所有子类共享同一个实例以复用连接池
     */
    protected final OkHttpClient httpClient = new OkHttpClient();

    /**
     * JSON序列化工具
     * 用于解析API响应的JSON数据
     */
    protected final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 执行HTTP请求并返回响应内容
     *
     * @param request HTTP请求对象
     * @return API响应的字符串内容
     * @throws IOException 当网络请求失败时抛出
     */
    protected String executeRequest(Request request) throws IOException {
        try (Response response = httpClient.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * 验证API响应的状态
     * 
     * @param status HTTP状态码或API自定义状态码
     * @param message 错误信息
     * @throws RuntimeException 当API调用失败时抛出，包含具体的错误信息
     */
    protected void validateResponse(int status, String message) {
        if (status != 200) {
            throw new RuntimeException("API调用失败: " + message);
        }
    }
}