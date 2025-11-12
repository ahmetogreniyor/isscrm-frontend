import React, { useEffect, useState } from "react";
import {
  Box,
  Typography,
  Grid,
  Card,
  CardContent,
  CircularProgress,
} from "@mui/material";
import axios from "axios";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

const Dashboard = () => {
  const [stats, setStats] = useState({
    dealers: 0,
    customers: 0,
    revenue: 0,
  });
  const [chartData, setChartData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [dealersRes, customersRes, tariffsRes] = await Promise.all([
          axios.get("http://localhost:8080/api/dealers"),
          axios.get("http://localhost:8080/api/customers"),
          axios.get("http://localhost:8080/api/tariffs"),
        ]);

        const dealers = dealersRes.data;
        const customers = customersRes.data;
        const tariffs = tariffsRes.data;

        // Ortalama fiyat
        const avgRevenue =
          tariffs.reduce((sum, t) => sum + (t.price || 0), 0) /
          (tariffs.length || 1);

        // Toplam ciro (örnek: müşteri sayısı * ortalama tarife fiyatı)
        const revenue = Math.round(customers.length * avgRevenue);

        // 🔹 Aylık revenue & yeni müşteri grafiği
        const months = [
          "Jan",
          "Feb",
          "Mar",
          "Apr",
          "May",
          "Jun",
          "Jul",
          "Aug",
          "Sep",
          "Oct",
          "Nov",
          "Dec",
        ];

        // createdAt üzerinden ay bazlı grupla
        const monthlyStats = months.map((m, index) => {
          const monthCustomers = customers.filter(
            (c) => new Date(c.createdAt).getMonth() === index
          ).length;
          const monthRevenue = monthCustomers * avgRevenue;
          return {
            month: m,
            revenue: Math.round(monthRevenue),
            customers: monthCustomers,
          };
        });

        setStats({
          dealers: dealers.length,
          customers: customers.length,
          revenue,
        });
        setChartData(monthlyStats);
      } catch (err) {
        console.error("Dashboard data fetch error:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" mt={10}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box p={4}>
      <Typography variant="h4" mb={3} fontWeight="bold">
        Dashboard Overview
      </Typography>

      {/* 🔹 Üst İstatistik Kartları */}
      <Grid container spacing={3} mb={4}>
        <Grid item xs={12} sm={4}>
          <Card sx={{ boxShadow: 3, borderRadius: 3 }}>
            <CardContent>
              <Typography variant="subtitle2" color="text.secondary">
                TOTAL DEALERS
              </Typography>
              <Typography variant="h4" fontWeight="bold">
                {stats.dealers}
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={4}>
          <Card sx={{ boxShadow: 3, borderRadius: 3 }}>
            <CardContent>
              <Typography variant="subtitle2" color="text.secondary">
                TOTAL CUSTOMERS
              </Typography>
              <Typography variant="h4" fontWeight="bold">
                {stats.customers}
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={4}>
          <Card sx={{ boxShadow: 3, borderRadius: 3 }}>
            <CardContent>
              <Typography variant="subtitle2" color="text.secondary">
                MONTHLY REVENUE ($)
              </Typography>
              <Typography variant="h4" fontWeight="bold">
                {stats.revenue.toLocaleString()}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* 🔸 Aylık Grafik */}
      <Card sx={{ p: 3, boxShadow: 3, borderRadius: 3 }}>
        <Typography variant="h6" mb={2}>
          Revenue & Customer Growth
        </Typography>
        <ResponsiveContainer width="100%" height={350}>
          <BarChart
            data={chartData}
            margin={{ top: 20, right: 30, left: 0, bottom: 5 }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="month" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Bar dataKey="revenue" fill="#1976d2" name="Revenue ($)" />
            <Bar dataKey="customers" fill="#90caf9" name="New Customers" />
          </BarChart>
        </ResponsiveContainer>
      </Card>
    </Box>
  );
};

export default Dashboard;
