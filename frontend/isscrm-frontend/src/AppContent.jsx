import React, { useEffect, useState, useContext } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import {
  AppBar,
  Toolbar,
  Typography,
  Box,
  IconButton,
  Tooltip,
  Menu,
  MenuItem,
  Avatar,
} from "@mui/material";
import AccessTimeIcon from "@mui/icons-material/AccessTime";
import Brightness4Icon from "@mui/icons-material/Brightness4";
import Brightness7Icon from "@mui/icons-material/Brightness7";
import { ThemeContext } from "./context/ThemeContext";
import MainLayout from "./layout/MainLayout";
import Dashboard from "./pages/Dashboard";
import Dealers from "./pages/Dealers";
import Customers from "./pages/Customers";
import Tariffs from "./pages/Tariffs";
import Profile from "./pages/Profile";
import Settings from "./pages/Settings";

const AppContent = () => {
  const [currentTime, setCurrentTime] = useState("");
  const [anchorEl, setAnchorEl] = useState(null);
  const { mode, toggleTheme } = useContext(ThemeContext);

  // 🕒 Saat
  useEffect(() => {
    const updateClock = () => {
      const now = new Date();
      setCurrentTime(now.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit", second: "2-digit" }));
    };
    const timer = setInterval(updateClock, 1000);
    return () => clearInterval(timer);
  }, []);

  const handleMenuOpen = (e) => setAnchorEl(e.currentTarget);
  const handleMenuClose = () => setAnchorEl(null);
  const goTo = (path) => {
    handleMenuClose();
    window.location.href = path;
  };
  const handleLogout = () => {
    handleMenuClose();
    if (window.confirm("Are you sure you want to logout?")) {
      window.location.href = "/";
    }
  };

  return (
    <Router>
      <AppBar position="fixed" sx={{ zIndex: (t) => t.zIndex.drawer + 1 }}>
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            ISS CRM Dashboard
          </Typography>

          {/* 🕒 Saat */}
          <Box sx={{ display: "flex", alignItems: "center", gap: 1, mr: 3 }}>
            <AccessTimeIcon fontSize="small" />
            <Typography variant="body2">{currentTime}</Typography>
          </Box>

          {/* 🌗 Tema geçiş düğmesi */}
          <Tooltip title={`Switch to ${mode === "light" ? "Dark" : "Light"} Mode`}>
            <IconButton onClick={toggleTheme} color="inherit">
              {mode === "light" ? <Brightness4Icon /> : <Brightness7Icon />}
            </IconButton>
          </Tooltip>

          {/* 👤 Profil Avatar */}
          <Tooltip title="Account Menu">
            <IconButton onClick={handleMenuOpen} sx={{ p: 0 }}>
              <Avatar
                alt="Ahmet Dumanlı"
                src="https://ui-avatars.com/api/?name=Ahmet+Dumanli&background=1976d2&color=fff"
              />
            </IconButton>
          </Tooltip>

          {/* Menü */}
          <Menu
            anchorEl={anchorEl}
            open={Boolean(anchorEl)}
            onClose={handleMenuClose}
            anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
            transformOrigin={{ vertical: "top", horizontal: "right" }}
          >
            <MenuItem disabled>
              <Typography variant="body2">
                Signed in as <strong>Ahmet Dumanlı</strong>
              </Typography>
            </MenuItem>
            <MenuItem onClick={() => goTo("/profile")}>Profile</MenuItem>
            <MenuItem onClick={() => goTo("/settings")}>Settings</MenuItem>
            <MenuItem onClick={handleLogout}>Logout</MenuItem>
          </Menu>
        </Toolbar>
      </AppBar>

      <MainLayout>
        <Routes>
          <Route path="/" element={<Navigate to="/dashboard" />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/dealers" element={<Dealers />} />
          <Route path="/customers" element={<Customers />} />
          <Route path="/tariffs" element={<Tariffs />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/settings" element={<Settings />} />
          <Route path="*" element={<Navigate to="/dashboard" />} />
        </Routes>
      </MainLayout>
    </Router>
  );
};

export default AppContent;
