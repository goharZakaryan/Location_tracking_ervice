package com.location.tracking.controller;

import com.location.tracking.entity.Location;
import com.location.tracking.model.LocationReport;
import com.location.tracking.service.LocationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public void updateLocation(@RequestBody Location location) {
        locationService.processLocation(location);
    }

    @GetMapping("/report/{userId}")
    public LocationReport getReport(
            @PathVariable String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime
    ) {
        return locationService.generateReport(userId, startTime, endTime);
    }
}
