package com.example.geoservice.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Location implements Serializable {
    private double lat;
    private double lng;
}