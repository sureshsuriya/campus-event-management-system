package com.campus.campus_event_management.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.campus.campus_event_management.model.Event;
import com.campus.campus_event_management.model.User;
import com.campus.campus_event_management.repository.EventRepository;
import com.campus.campus_event_management.repository.UserRepository;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:5173")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    // ==========================
    // STAFF VIEW ALL EVENTS
    // ==========================
    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // ==========================
    // STUDENT VIEW ONLY BOOKED
    // ==========================
    @GetMapping
    public List<Event> getBookedEvents() {
        return eventRepository.findByStatus("BOOKED");
    }

    // ==========================
    // CREATE EVENT
    // ==========================
    @PostMapping(consumes = "multipart/form-data")
    public Event create(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String date,
            @RequestParam String hall,
            @RequestParam String email,
            @RequestParam(required = false) String registrationLink,
            @RequestParam("image") MultipartFile image
    ) throws IOException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = uploadDir.resolve(fileName);
        Files.write(filePath, image.getBytes());

        // Check if already booked
        List<Event> bookedEvents =
                eventRepository.findByHallAndDateAndStatus(
                        hall,
                        date,
                        "BOOKED"
                );

        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setDate(date);
        event.setHall(hall);
        event.setCreatedBy(user.getName());
        event.setImageUrl("http://localhost:8080/uploads/" + fileName);

        if (registrationLink != null && !registrationLink.isEmpty()) {
            event.setRegistrationLink(registrationLink);
        }

        // 🔥 QUEUE LOGIC
        if (bookedEvents.isEmpty()) {
            event.setStatus("BOOKED");
        } else {
            event.setStatus("WAITING");
        }

        return eventRepository.save(event);
    }

    // ==========================
    // CANCEL EVENT
    // ==========================
    @PutMapping("/cancel/{id}")
    public Event cancelEvent(
            @PathVariable String id,
            @RequestParam String userEmail
    ) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!event.getCreatedBy().equals(user.getName())) {
            throw new RuntimeException("You cannot cancel this event.");
        }

        event.setStatus("CANCELLED");
        eventRepository.save(event);

        // 🔥 PROMOTE FIRST WAITING
        List<Event> waitingEvents =
                eventRepository.findByHallAndDateAndStatus(
                        event.getHall(),
                        event.getDate(),
                        "WAITING"
                );

        if (!waitingEvents.isEmpty()) {
            Event next = waitingEvents.get(0); // FIFO
            next.setStatus("BOOKED");
            eventRepository.save(next);
        }

        return event;
    }
}