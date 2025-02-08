package com.location.tracking.service;

import com.location.tracking.entity.Location;
import com.location.tracking.model.LocationReport;

import java.time.LocalDateTime;

public interface LocationService {
    void processLocation(Location location);
    LocationReport generateReport(String userId, LocalDateTime startTime, LocalDateTime endTime);
}
