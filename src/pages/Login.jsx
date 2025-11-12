import React, { useState } from "react";
import {
  Box,
  Button,
  Card,
  CardContent,
  TextField,
  Typography,
  CircularProgress,
} from "@mui/material";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      // 🔹 1️⃣ Backend'e login isteği gönder
      const res = await axios.post("http://localhost:8080/api/auth/login", {
        email,
        password,
      });

      if (res.data?.token) {
        // 🔹 2️⃣ Token ve session süresi ayarla
        const expiresIn = 30 * 60 * 1000; // 30 dakika
        const expiryTime = Date.now() + expiresIn;

        localStorage.setItem("token", res.data.token);
        localStorage.setItem("token_expiry", expiryTime.toString());

        // 🔹 3️⃣ Backend'den gelen user bilgisi veya fallback oluştur
        let userData = res.data.user || {
          fullName: email.split("@")[0],
          username: email.split("@")[0],
          email: email,
          role: "USER",
        };

        // 🔹 4️⃣ Eğer user.id yoksa email ile backend'den getir
        if (!userData.id) {
          try {
            const userRes = await axios.get(
              `http://localhost:8080/api/users/by-email/${email}`
            );
            if (userRes.data?.id) {
              userData.id = userRes.data.id;
              userData.fullName = userRes.data.fullName || userData.fullName;
              userData.phone = userRes.data.phone || "";
              userData.role = userRes.data.role || "USER";
            }
          } catch (err) {
            console.warn("⚠️ User lookup by email failed:", err);
          }
        }

        // 🔹 5️⃣ LocalStorage’a kaydet
        localStorage.setItem("user", JSON.stringify(userData));

        // 🔹 6️⃣ Dashboard'a yönlendir
        navigate("/dashboard");
      } else {
        setError("Invalid response from server");
      }
    } catch (err) {
      console.error("❌ Login error:", err);
      setError("Invalid email or password");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box
      display="flex"
      justifyContent="center"
      alignItems="center"
      height="100vh"
      bgcolor="#f5f5f5"
    >
      <Card sx={{ width: 400, boxShadow: 3, borderRadius: 3 }}>
        <CardContent>
          <Typography
            variant="h5"
            mb={3}
            fontWeight="bold"
            textAlign="center"
          >
            ISS CRM Login
          </Typography>

          <form onSubmit={handleLogin}>
            <TextField
              label="Email"
              variant="outlined"
              fullWidth
              margin="normal"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <TextField
              label="Password"
              type="password"
              variant="outlined"
              fullWidth
              margin="normal"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />

            {error && (
              <Typography color="error" mt={1} fontSize={14}>
                {error}
              </Typography>
            )}

            <Button
              type="submit"
              variant="contained"
              fullWidth
              sx={{ mt: 3, borderRadius: 2 }}
              disabled={loading}
            >
              {loading ? <CircularProgress size={24} /> : "Login"}
            </Button>
          </form>
        </CardContent>
      </Card>
    </Box>
  );
};

export default Login;
