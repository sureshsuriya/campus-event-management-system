package com.campus.campus_event_management.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "hall_bookings")
public class HallBooking {

    @Id
    private String id;

    private String hallName;
    private String staffName;
    private String bookingDate;
    private String startTime;
    private String endTime;

    private String status; // BOOKED, WAITING, CANCELLED

    public HallBooking() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getHallName() { return hallName; }
    public void setHallName(String hallName) { this.hallName = hallName; }

    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}