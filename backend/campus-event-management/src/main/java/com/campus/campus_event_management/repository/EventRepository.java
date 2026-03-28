package com.campus.campus_event_management.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.campus.campus_event_management.model.Event;

public interface EventRepository extends MongoRepository<Event, String> {

    // Student sees only BOOKED
    List<Event> findByStatus(String status);

    // Check booked for hall + date
    List<Event> findByHallAndDateAndStatus(
            String hall,
            String date,
            String status
    );
}