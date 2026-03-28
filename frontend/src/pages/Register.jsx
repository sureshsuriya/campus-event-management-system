import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { registerUser } from "../services/api";
import "./Register.css";

export default function Register() {
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("STUDENT");
  const [loading, setLoading] = useState(false);

  const handleRegister = async () => {
    // ✅ Full Name is now REQUIRED
    if (!name || !email || !password) {
      alert("Full Name, Email and Password are required");
      return;
    }

    try {
      setLoading(true);

      await registerUser({
        name,      // REQUIRED
        email,
        password,
        role,
      });

      alert("Registered successfully ✅ Please login.");
      navigate("/");
    } catch (err) {
      console.error(err);
      alert("Registration failed ❌ Email may already exist");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="register-page">
      <div className="register-card">
        <h2>Create Account</h2>
        <p className="subtitle">Campus Event Management</p>

        <input
          type="text"
          placeholder="Full Name *"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />

        <input
          type="email"
          placeholder="Email *"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          type="password"
          placeholder="Password *"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <select value={role} onChange={(e) => setRole(e.target.value)}>
          <option value="STUDENT">Student</option>
          <option value="STAFF">Staff</option>
        </select>

        <button onClick={handleRegister} disabled={loading}>
          {loading ? "Registering..." : "Register"}
        </button>

        <p className="back-link" onClick={() => navigate("/")}>
          ← Back to Login
        </p>
      </div>
    </div>
  );
}
