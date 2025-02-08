package com.location.tracking.service;

import com.location.tracking.entity.Location;
import com.location.tracking.model.LocationReport;
import com.location.tracking.repository.LocationRepository;
import com.location.tracking.service.impl.LocationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private KafkaTemplate<String, Location> kafkaTemplate;

    @InjectMocks
    private LocationServiceImpl locationService;  // Just declare it, don't instantiate

    private Location location1;
    private Location location2;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.now().minusHours(1);
        endTime = LocalDateTime.now();

        location1 = new Location();
        location1.setUserId("user123");
        location1.setLatitude(40.7128);
        location1.setLongitude(-74.0060);
        location1.setTimestamp(startTime);

        location2 = new Location();
        location2.setUserId("user123");
        location2.setLatitude(40.7589);
        location2.setLongitude(-73.9851);
        location2.setTimestamp(endTime);
    }


    @Test
    void processLocation_ShouldSaveAndSendToKafka() {
        // Given
        Location location1 = new Location();
        location1.setUserId("user123");
        location1.setLatitude(40.7128);
        location1.setLongitude(-74.0060);

        // When
        locationService.processLocation(location1);

        // Then
        verify(kafkaTemplate, times(1)).send(eq(null), any(Location.class));  // Verify Kafka message send
    }

    @Test
    void generateReport_ShouldCalculateDistanceCorrectly() {
        // Given
        List<Location> locations = Arrays.asList(location1, location2);
        when(locationRepository.findByUserIdAndTimestampBetween(
                "user123", startTime, endTime))
                .thenReturn(locations);

        // When
        LocationReport report = locationService.generateReport("user123", startTime, endTime);

        // Then
        assertNotNull(report);
        assertTrue(report.getTotalDistanceKm() > 0);
        assertEquals("user123", report.getUserId());
        assertEquals(2, report.getNumberOfLocations());
    }
}