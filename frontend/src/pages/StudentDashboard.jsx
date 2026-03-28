import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getEvents } from "../services/api";
import "./StudentDashboard.css";

export default function StudentDashboard() {

  const navigate = useNavigate();
  const [events, setEvents] = useState([]);

  useEffect(() => {

    const role = localStorage.getItem("role");

    // 🔒 Protect Route
    if (role !== "STUDENT") {
      navigate("/");
      return;
    }

    fetchEvents();

  }, []);

  const fetchEvents = async () => {
    try {
      const res = await getEvents();  // ✅ Only BOOKED
      setEvents(res.data || []);
    } catch (error) {
      console.error("Error fetching events", error);
    }
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate("/");
  };

  return (
    <div className="student-container">

      <div className="student-header">
        <h1>Student Dashboard</h1>
        <button onClick={handleLogout}>Logout</button>
      </div>

      <div className="event-list">

        {events.length === 0 ? (
          <p>No events available</p>
        ) : (
          events.map((event) => (
            <div key={event.id} className="event-card">

              {event.imageUrl && (
                <img src={event.imageUrl} alt={event.title} />
              )}

              <h3>{event.title}</h3>

              <p>{event.description}</p>

              <p><strong>Date:</strong> {event.date}</p>
              <p><strong>Hall:</strong> {event.hall}</p>
              <p><strong>Organized by:</strong> {event.createdBy}</p>

              {event.registrationLink && (
                <a
                  href={event.registrationLink}
                  target="_blank"
                  rel="noreferrer"
                  className="register-btn"
                >
                  Register
                </a>
              )}

            </div>
          ))
        )}

      </div>

    </div>
  );
}