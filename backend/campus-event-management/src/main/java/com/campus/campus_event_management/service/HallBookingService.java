package com.campus.campus_event_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.campus.campus_event_management.model.HallBooking;
import com.campus.campus_event_management.repository.HallBookingRepository;

@Service
public class HallBookingService {

    private final HallBookingRepository repository;

    public HallBookingService(HallBookingRepository repository) {
        this.repository = repository;
    }

    public HallBooking bookHall(HallBooking booking) {

        List<HallBooking> existing =
                repository.findByHallNameAndBookingDateAndStatus(
                        booking.getHallName(),
                        booking.getBookingDate(),
                        "BOOKED"
                );

        boolean conflict = existing.stream().anyMatch(b ->
                booking.getStartTime().compareTo(b.getEndTime()) < 0 &&
                booking.getEndTime().compareTo(b.getStartTime()) > 0
        );

        if (conflict) {
            booking.setStatus("WAITING");
        } else {
            booking.setStatus("BOOKED");
        }

        return repository.save(booking);
    }

    public void cancelBooking(String id) {

        HallBooking booking = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus("CANCELLED");
        repository.save(booking);

        List<HallBooking> waitingList =
                repository.findByHallNameAndBookingDateAndStatusOrderByIdAsc(
                        booking.getHallName(),
                        booking.getBookingDate(),
                        "WAITING"
                );

        for (HallBooking waiting : waitingList) {

            boolean conflict =
                    repository.findByHallNameAndBookingDateAndStatus(
                            waiting.getHallName(),
                            waiting.getBookingDate(),
                            "BOOKED"
                    ).stream().anyMatch(b ->
                            waiting.getStartTime().compareTo(b.getEndTime()) < 0 &&
                            waiting.getEndTime().compareTo(b.getStartTime()) > 0
                    );

            if (!conflict) {
                waiting.setStatus("BOOKED");
                repository.save(waiting);
                break;
            }
        }
    }
}