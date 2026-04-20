package com.example.service.service.impl;

import com.example.service.dto.response.IpLocationResponseDto;
import com.example.service.service.GeoLocationService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoLocationServiceImpl implements GeoLocationService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public IpLocationResponseDto getLocationByIp(String ip) {
        try {
            String url = "http://ip-api.com/json/" + ip;
            IpLocationResponseDto response =
                    restTemplate.getForObject(url, IpLocationResponseDto.class);

            if (response != null && "success".equals(response.getStatus())) {
                return response;
            }

        } catch (Exception e) {
            // log error nếu cần
        }

        return null;
    }

    @Override
    public String buildLocationString(IpLocationResponseDto dto) {
        if (dto == null) return "Unknown location";

        return String.format("%s, %s", dto.getCity(), dto.getCountry());
    }

    @Override
    public String buildGoogleMapUrl(IpLocationResponseDto dto) {
        if (dto == null) return "#";

        return String.format("https://www.google.com/maps?q=%s,%s",
                dto.getLat(),
                dto.getLon()
        );
    }
}