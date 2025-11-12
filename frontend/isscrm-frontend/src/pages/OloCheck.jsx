// src/pages/OloCheck.jsx
import React, { useState } from "react";
import {
  Box,
  Typography,
  TextField,
  Button,
  CircularProgress,
  Card,
  CardContent,
  Grid,
  Alert,
} from "@mui/material";
import axios from "axios";

const API_URL = "http://localhost:8080/api/olo/check";

const OloCheck = () => {
  const [addressCode, setAddressCode] = useState("");
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");

  const handleCheck = async () => {
    if (!addressCode.trim()) {
      setError("Lütfen bir adres kodu giriniz.");
      return;
    }
    setError("");
    setLoading(true);
    setResult(null);

    try {
      const res = await axios.get(`${API_URL}?addressCode=${addressCode}`);
      setResult(res.data);
    } catch (err) {
      setError("Altyapı sorgulaması başarısız oldu.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box p={3}>
      <Typography variant="h5" mb={2}>
        🌐 Türk Telekom OLO Altyapı Sorgulama
      </Typography>

      <Box display="flex" gap={2} alignItems="center" mb={3}>
        <TextField
          label="Adres Kodu (BBK)"
          variant="outlined"
          value={addressCode}
          onChange={(e) => setAddressCode(e.target.value)}
          fullWidth
        />
        <Button
          variant="contained"
          color="primary"
          onClick={handleCheck}
          disabled={loading}
          sx={{ height: "56px" }}
        >
          {loading ? "Sorgulanıyor..." : "Sorgula"}
        </Button>
      </Box>

      {loading && (
        <Box textAlign="center" mt={2}>
          <CircularProgress />
          <Typography variant="body2" mt={1}>
            Altyapı bilgileri getiriliyor...
          </Typography>
        </Box>
      )}

      {error && (
        <Alert severity="error" sx={{ mt: 2 }}>
          {error}
        </Alert>
      )}

      {result && (
        <Card sx={{ mt: 3, p: 2 }}>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              📍 Adres Kodu: {result.addressCode}
            </Typography>

            <Grid container spacing={2}>
              <Grid item xs={12} sm={6} md={4}>
                <Alert
                  severity={result.fiberAvailable ? "success" : "error"}
                  icon={false}
                >
                  Fiber:{" "}
                  {result.fiberAvailable ? "Uygun ✅" : "Yok ❌"}
                </Alert>
              </Grid>
              <Grid item xs={12} sm={6} md={4}>
                <Alert
                  severity={result.vdslAvailable ? "success" : "error"}
                  icon={false}
                >
                  VDSL:{" "}
                  {result.vdslAvailable ? "Uygun ✅" : "Yok ❌"}
                </Alert>
              </Grid>
              <Grid item xs={12} sm={6} md={4}>
                <Alert
                  severity={result.adslAvailable ? "success" : "error"}
                  icon={false}
                >
                  ADSL:{" "}
                  {result.adslAvailable ? "Uygun ✅" : "Yok ❌"}
                </Alert>
              </Grid>
            </Grid>

            <Typography variant="body1" mt={3}>
              ⚡ Maksimum Hız:{" "}
              <b style={{ color: "#1976d2" }}>{result.maxSpeed}</b>
            </Typography>

            <Typography variant="body2" color="text.secondary" mt={1}>
              Son Sorgu:{" "}
              {new Date(result.lastChecked).toLocaleString("tr-TR")}
            </Typography>
          </CardContent>
        </Card>
      )}
    </Box>
  );
};

export default OloCheck;
