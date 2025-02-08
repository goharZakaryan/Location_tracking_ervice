package com.location.tracking.repository;



import com.location.tracking.entity.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    void findByUserIdAndTimestampBetween_ShouldReturnLocations() {
        // Given
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();

        Location location = new Location();
        location.setUserId("user123");
        location.setLatitude(40.7128);
        location.setLongitude(-74.0060);
        location.setTimestamp(startTime.plusMinutes(30));
        locationRepository.save(location);

        // When
        List<Location> locations = locationRepository.findByUserIdAndTimestampBetween(
                "user123", startTime, endTime);

        // Then
        assertFalse(locations.isEmpty());
        assertEquals(1, locations.size());
        assertEquals("user123", locations.get(0).getUserId());
    }
}
