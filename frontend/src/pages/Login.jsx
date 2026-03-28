import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../services/api";
import "./Login.css";

export default function Login() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [selectedRole, setSelectedRole] = useState("STUDENT");

  const handleLogin = async () => {
    if (!email || !password) {
      alert("Please fill all fields");
      return;
    }

    try {
      const res = await loginUser({
        email,
        password,
        role: selectedRole
      });

      const user = res.data;

      const role = user.role?.toUpperCase();

      // ✅ STORE EVERYTHING
      localStorage.setItem("email", user.email);
      localStorage.setItem("name", user.name);
      localStorage.setItem("role", role);

      console.log("Stored Email:", user.email);
      console.log("Stored Name:", user.name);
      console.log("Stored Role:", role);

      if (role === "STUDENT") {
        navigate("/student");
      } else if (role === "STAFF") {
        navigate("/staff");
      } else {
        alert("Unknown role");
      }

    } catch (error) {
      alert(error.response?.data || "Login failed ❌");
    }
  };

  return (
    <div className="auth-bg">
      <div className="auth-card">
        <h2>Campus Event Management</h2>

        <select
          value={selectedRole}
          onChange={(e) => setSelectedRole(e.target.value)}
        >
          <option value="STUDENT">Student Login</option>
          <option value="STAFF">Staff Login</option>
        </select>

        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button onClick={handleLogin}>Login</button>

        <p className="link" onClick={() => navigate("/register")}>
          New user? Register here
        </p>
      </div>
    </div>
  );
}