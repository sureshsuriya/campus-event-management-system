import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { createEvent, getAllEvents, API } from "../services/api";
import "./StaffDashboard.css";

export default function StaffDashboard() {

  const navigate = useNavigate();

  const currentUserEmail = localStorage.getItem("email") || "";
  const currentUserName = localStorage.getItem("name") || "";

  const [form, setForm] = useState({
    title: "",
    description: "",
    date: "",
    hall: "",
    registrationLink: ""
  });

  const [poster, setPoster] = useState(null);
  const [events, setEvents] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const role = localStorage.getItem("role");

    // 🔒 Protect Route
    if (role !== "STAFF") {
      navigate("/");
      return;
    }

    fetchEvents();
  }, []);

  const fetchEvents = async () => {
    try {
      const res = await getAllEvents();
      setEvents(res.data || []);
    } catch (err) {
      console.error("Error fetching events", err);
    }
  };

  const handleCreateEvent = async () => {

    if (!currentUserEmail) {
      setError("User not logged in properly.");
      return;
    }

    if (!form.title || !form.description || !form.date || !form.hall || !poster) {
      setError("Please fill all required fields.");
      return;
    }

    try {
      setLoading(true);
      setError("");

      const formData = new FormData();
      formData.append("title", form.title);
      formData.append("description", form.description);
      formData.append("date", form.date);
      formData.append("hall", form.hall);
      formData.append("email", currentUserEmail);
      formData.append("image", poster);

      if (form.registrationLink) {
        formData.append("registrationLink", form.registrationLink);
      }

      await createEvent(formData);

      setForm({
        title: "",
        description: "",
        date: "",
        hall: "",
        registrationLink: ""
      });

      setPoster(null);
      fetchEvents();

    } catch (err) {
      console.error(err);
      setError(err.response?.data || "Error creating event.");
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = async (id) => {
    try {
      await API.put(`/events/cancel/${id}?userEmail=${currentUserEmail}`);
      fetchEvents();
    } catch (err) {
      alert("You cannot cancel this event.");
    }
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate("/");
  };

  return (
    <div className="staff-container">

      <div className="header">
        <h1>Staff Dashboard</h1>
        <button className="logout-btn" onClick={handleLogout}>
          Logout
        </button>
      </div>

      {/* CREATE EVENT */}
      <div className="card">
        <h3>Create Event</h3>

        <input
          type="text"
          placeholder="Event Title"
          value={form.title}
          onChange={(e) => setForm({ ...form, title: e.target.value })}
        />

        <textarea
          placeholder="Event Description"
          value={form.description}
          onChange={(e) => setForm({ ...form, description: e.target.value })}
        />

        <input
          type="date"
          value={form.date}
          onChange={(e) => setForm({ ...form, date: e.target.value })}
        />

        <select
          value={form.hall}
          onChange={(e) => setForm({ ...form, hall: e.target.value })}
        >
          <option value="">Select Hall</option>
          <option value="Old Auditorium">Old Auditorium</option>
          <option value="New Auditorium">New Auditorium</option>
          <option value="Golden Jubilee Hall">Golden Jubilee Hall</option>
          <option value="CS Seminar Hall">CS Seminar Hall</option>
        </select>

        <input
          type="file"
          onChange={(e) => setPoster(e.target.files[0])}
        />

        <input
          type="text"
          placeholder="Registration Link (Optional)"
          value={form.registrationLink}
          onChange={(e) => setForm({ ...form, registrationLink: e.target.value })}
        />

        <button onClick={handleCreateEvent} disabled={loading}>
          {loading ? "Creating..." : "Create Event"}
        </button>

        {error && <div className="error-box">{error}</div>}
      </div>

      {/* EVENTS LIST */}
      <div className="card">
        <h3>My Events</h3>

        {events.length === 0 && <p>No events created yet.</p>}

        {events.map((event) => {

          const safeStatus = event.status || "WAITING";

          return (
            <div key={event.id} className="event-row">

              <div className="event-info">
                <h4>{event.title}</h4>
                <p>{event.date} | {event.hall}</p>
                <small>Created by: {event.createdBy}</small>
              </div>

              <div className="event-actions">

                <span className={`status ${safeStatus.toLowerCase()}`}>
                  {safeStatus}
                </span>

                {/* 🔥 CANCEL BUTTON ONLY FOR CREATOR */}
                {safeStatus === "BOOKED" &&
                 event.createdBy?.trim() === currentUserName?.trim() && (
                  <button
                    className="cancel-btn"
                    onClick={() => handleCancel(event.id)}
                  >
                    Cancel
                  </button>
                )}

              </div>

            </div>
          );
        })}
      </div>

    </div>
  );
}