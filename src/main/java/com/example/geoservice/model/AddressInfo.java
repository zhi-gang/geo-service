package com.example.geoservice.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 地址信息类
 * <p>
 * 包含地址的详细信息，如经纬度、省市区等
 * </p>
 */
@Data
public class AddressInfo implements Serializable {
    /**
     * 经度
     */
    private double longitude;

    /**
     * 纬度
     */
    private double latitude;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 街道
     */
    private String street;

    /**
     * 门牌号
     */
    private String streetNumber;

    /**
     * 完整地址
     */
    private String formattedAddress;

    /**
     * 邮政编码
     */
    private String postalCode;

    /**
     * 置信度，表示地址匹配的准确度
     */
    private double confidence;
}