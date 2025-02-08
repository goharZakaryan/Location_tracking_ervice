package com.location.tracking.service.impl;

import com.location.tracking.entity.Location;
import com.location.tracking.model.LocationReport;
import com.location.tracking.repository.LocationRepository;
import com.location.tracking.service.LocationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    @Value("${kafka.topic.location-updates}")
    private String locationUpdatesTopic;

    private final KafkaTemplate<String, Location> kafkaTemplate;

    public LocationServiceImpl(LocationRepository locationRepository, KafkaTemplate<String, Location> kafkaTemplate) {
        this.locationRepository = locationRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void processLocation(Location location) {
        kafkaTemplate.send(locationUpdatesTopic, location);
    }

    @Scheduled(fixedRate = 3600000)
    @Override
    public LocationReport generateReport(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Location> locations = locationRepository.findByUserIdAndTimestampBetween(
                userId, startTime, endTime);

        double totalDistance = calculateTotalDistance(locations);

        LocationReport report = new LocationReport();
        report.setUserId(userId);
        report.setTotalDistanceKm(totalDistance);
        report.setStartTime(startTime);
        report.setEndTime(endTime);
        report.setNumberOfLocations(locations.size());

        return report;
    }

    private double calculateTotalDistance(List<Location> locations) {
        double totalDistance = 0;

        for (int i = 0; i < locations.size() - 1; i++) {
            Location loc1 = locations.get(i);
            Location loc2 = locations.get(i + 1);
            totalDistance += calculateHaversineDistance(
                    loc1.getLatitude(), loc1.getLongitude(),
                    loc2.getLatitude(), loc2.getLongitude()
            );
        }

        return totalDistance;
    }

    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
