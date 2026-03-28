import axios from "axios";

export const API = axios.create({
  baseURL: "http://localhost:8081/api",
});

// 🔥 AUTH
export const loginUser = (data) =>
  API.post("/auth/login", data);

export const registerUser = (data) =>
  API.post("/auth/register", data);

// 🔥 EVENTS
export const getEvents = () =>
  API.get("/events"); // student

export const getAllEvents = () =>
  API.get("/events/all"); // staff

export const createEvent = (formData) =>
  API.post("/events", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });