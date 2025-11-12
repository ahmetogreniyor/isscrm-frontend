import React from "react";
import {
  Box,
  Typography,
  Switch,
  FormControlLabel,
  Button,
} from "@mui/material";
import TranslateIcon from "@mui/icons-material/Translate";
import SaveIcon from "@mui/icons-material/Save";
import LightModeIcon from "@mui/icons-material/LightMode";
import NotificationsActiveIcon from "@mui/icons-material/NotificationsActive";

const Settings = () => {
  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h5" sx={{ mb: 2 }}>
        <TranslateIcon sx={{ mr: 1, verticalAlign: "middle" }} />
        Settings
      </Typography>

      <FormControlLabel
        control={<Switch defaultChecked />}
        label={
          <>
            <LightModeIcon sx={{ mr: 1, verticalAlign: "middle" }} />
            Dark Mode
          </>
        }
      />

      <FormControlLabel
        control={<Switch />}
        label={
          <>
            <NotificationsActiveIcon sx={{ mr: 1, verticalAlign: "middle" }} />
            Notifications
          </>
        }
      />

      <Box sx={{ mt: 3 }}>
        <Button variant="contained" startIcon={<SaveIcon />}>
          Save Settings
        </Button>
      </Box>
    </Box>
  );
};

export default Settings;
