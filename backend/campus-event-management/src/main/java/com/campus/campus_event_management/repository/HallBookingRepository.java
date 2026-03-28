package com.campus.campus_event_management.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.campus.campus_event_management.model.HallBooking;

public interface HallBookingRepository extends MongoRepository<HallBooking, String> {

    List<HallBooking> findByHallNameAndBookingDateAndStatus(
            String hallName,
            String bookingDate,
            String status
    );

    List<HallBooking> findByHallNameAndBookingDateAndStatusOrderByIdAsc(
            String hallName,
            String bookingDate,
            String status
    );
}