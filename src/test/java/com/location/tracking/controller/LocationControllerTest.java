package com.location.tracking.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.location.tracking.entity.Location;
import com.location.tracking.model.LocationReport;
import com.location.tracking.service.LocationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationController.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void updateLocation_ShouldReturn200() throws Exception {
        // Given
        Location location = new Location();
        location.setUserId("user123");
        location.setLatitude(40.7128);
        location.setLongitude(-74.0060);

        // When/Then
        mockMvc.perform(post("/api/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(location)))
                .andExpect(status().isOk());
    }

    @Test
    void getReport_ShouldReturnReport() throws Exception {
        // Given
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();

        LocationReport report = new LocationReport();
        report.setUserId("user123");
        report.setTotalDistanceKm(10.5);
        report.setStartTime(startTime);
        report.setEndTime(endTime);
        report.setNumberOfLocations(5);

        when(locationService.generateReport(any(), any(), any())).thenReturn(report);

        // When/Then
        mockMvc.perform(get("/api/locations/report/user123")
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.totalDistanceKm").value(10.5))
                .andExpect(jsonPath("$.numberOfLocations").value(5));
    }
}

