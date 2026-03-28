package com.campus.campus_event_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campus.campus_event_management.model.HallBooking;
import com.campus.campus_event_management.service.HallBookingService;

@RestController
@RequestMapping("/api/hall")
@CrossOrigin(origins = "http://localhost:5173")
public class HallBookingController {

    private final HallBookingService service;

    public HallBookingController(HallBookingService service) {
        this.service = service;
    }

    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestBody HallBooking booking) {
        return ResponseEntity.ok(service.bookHall(booking));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancel(@PathVariable String id) {
        service.cancelBooking(id);
        return ResponseEntity.ok("Booking Cancelled Successfully");
    }
}