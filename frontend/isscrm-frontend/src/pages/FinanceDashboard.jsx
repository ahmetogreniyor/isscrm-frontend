import React, { useEffect, useState } from "react";
import {
  Card,
  CardContent,
  Typography,
  Grid,
  CircularProgress,
} from "@mui/material";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";
import axios from "axios";
import SockJS from "sockjs-client";
import { over } from "stompjs";

// ✅ Global tanım (Vite ortamında “global is not defined” hatasını engeller)
if (typeof window !== "undefined") {
  window.global = window;
}

const BASE_URL = "http://localhost:8080/api/finance";
const WS_URL = "http://localhost:8080/ws";

const FinanceDashboard = () => {
  const [summary, setSummary] = useState(null);
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [stompClient, setStompClient] = useState(null);

  // 📊 Veriyi backend'den al
  const loadData = async () => {
    try {
      const response = await axios.get(`${BASE_URL}/summary`);
      setSummary(response.data);
      const histResponse = await axios.get(`${BASE_URL}/history`);
      setHistory(histResponse.data);
    } catch (error) {
      console.error("Finance data load failed:", error);
    } finally {
      setLoading(false);
    }
  };

  // 🔄 WebSocket Bağlantısı (Spring Boot FinanceUpdatePublisher ile)
  const connectWebSocket = () => {
    try {
      const socket = new SockJS(WS_URL);
      const stomp = over(socket);

      stomp.connect({}, () => {
        console.log("✅ WebSocket connected to /ws");
        stomp.subscribe("/topic/finance-updates", (message) => {
          if (message.body) {
            const updated = JSON.parse(message.body);
            console.log("📈 Live update:", updated);
            setSummary(updated);
          }
        });
      });

      setStompClient(stomp);
    } catch (err) {
      console.error("WebSocket connection failed:", err);
    }
  };

  // 🧠 İlk yüklemede veriyi al ve WS başlat
  useEffect(() => {
    loadData();
    connectWebSocket();

    // cleanup
    return () => {
      if (stompClient) {
        stompClient.disconnect();
        console.log("WebSocket disconnected");
      }
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <CircularProgress />
      </div>
    );
  }

  return (
    <div className="p-6">
      <Typography variant="h5" gutterBottom>
        💰 Finance Dashboard
      </Typography>

      <Grid container spacing={3}>
        {/* 🧾 Finance Summary */}
        <Grid item xs={12} md={4}>
          <Card className="shadow-lg rounded-2xl">
            <CardContent>
              <Typography variant="h6">Total Revenue</Typography>
              <Typography variant="h4" color="green">
                ${summary?.totalRevenue?.toLocaleString() ?? 0}
              </Typography>

              <Typography variant="h6" sx={{ mt: 2 }}>
                Pending Invoices
              </Typography>
              <Typography variant="h5" color="orange">
                {summary?.pendingCount ?? 0}
              </Typography>

              <Typography variant="h6" sx={{ mt: 2 }}>
                Paid Invoices
              </Typography>
              <Typography variant="h5" color="blue">
                {summary?.paidCount ?? 0}
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        {/* 📈 Revenue History Chart */}
        <Grid item xs={12} md={8}>
          <Card className="shadow-lg rounded-2xl">
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Revenue Over Time
              </Typography>
              <ResponsiveContainer width="100%" height={300}>
                <LineChart data={history}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="month" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Line
                    type="monotone"
                    dataKey="revenue"
                    stroke="#4CAF50"
                    strokeWidth={3}
                    activeDot={{ r: 6 }}
                  />
                </LineChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </div>
  );
};

export default FinanceDashboard;
