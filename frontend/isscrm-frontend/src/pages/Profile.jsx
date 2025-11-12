import React, { useEffect, useState } from "react";
import {
  Box,
  Typography,
  Paper,
  Avatar,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
} from "@mui/material";
import api from "../api";

const Profile = () => {
  const [user, setUser] = useState({
    name: "",
    email: "",
    role: "",
    avatar: "",
  });

  const [open, setOpen] = useState(false);
  const [formValues, setFormValues] = useState({ name: "", email: "", role: "" });

  // 🔹 Kullanıcı bilgisini localStorage'dan çek
  useEffect(() => {
    const savedUser = JSON.parse(localStorage.getItem("user"));
    if (savedUser) {
      setUser(savedUser);
      setFormValues(savedUser);
    }
  }, []);

  // 🔹 Profil güncelleme işlemi
  const handleSave = () => {
    api
      .put("/api/user/profile", formValues)
      .then((res) => {
        localStorage.setItem("user", JSON.stringify(res.data));
        setUser(res.data);
        setOpen(false);
      })
      .catch((err) => console.error("Profile update error:", err));
  };

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h5" sx={{ mb: 3, fontWeight: "bold" }}>
        User Profile
      </Typography>

      <Paper
        elevation={3}
        sx={{
          p: 4,
          maxWidth: 500,
          margin: "auto",
          textAlign: "center",
          backgroundColor: "#F9FAFB",
        }}
      >
        <Avatar
          src={user.avatar || "/static/images/avatar/1.jpg"}
          alt={user.name}
          sx={{
            width: 100,
            height: 100,
            margin: "auto",
            mb: 2,
            border: "2px solid #0284C7",
          }}
        />
        <Typography variant="h6">{user.name || "User"}</Typography>
        <Typography variant="body2" color="text.secondary">
          {user.email || "No email available"}
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
          {user.role ? `Role: ${user.role}` : ""}
        </Typography>

        <Button
          variant="contained"
          sx={{ mt: 3 }}
          onClick={() => setOpen(true)}
        >
          Edit Profile
        </Button>
      </Paper>

      {/* 🔹 Profil Güncelleme Dialogu */}
      <Dialog open={open} onClose={() => setOpen(false)} fullWidth maxWidth="sm">
        <DialogTitle>Edit Profile</DialogTitle>
        <DialogContent sx={{ pt: 2 }}>
          <TextField
            label="Name"
            fullWidth
            margin="normal"
            value={formValues.name}
            onChange={(e) => setFormValues({ ...formValues, name: e.target.value })}
          />
          <TextField
            label="Email"
            fullWidth
            margin="normal"
            value={formValues.email}
            onChange={(e) => setFormValues({ ...formValues, email: e.target.value })}
          />
          <TextField
            label="Role"
            fullWidth
            margin="normal"
            value={formValues.role}
            onChange={(e) => setFormValues({ ...formValues, role: e.target.value })}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpen(false)}>Cancel</Button>
          <Button variant="contained" onClick={handleSave}>
            Save
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default Profile;
