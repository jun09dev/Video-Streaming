package com.example.service.service;


import com.example.service.dto.response.IpLocationResponseDto;

public interface GeoLocationService {
    IpLocationResponseDto getLocationByIp(String ip);

    String buildLocationString(IpLocationResponseDto dto);

    String buildGoogleMapUrl(IpLocationResponseDto dto);
}
