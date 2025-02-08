package com.location.tracking.repository;

import com.location.tracking.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByUserIdAndTimestampBetween(String userId, LocalDateTime startTime, LocalDateTime endTime);
}
