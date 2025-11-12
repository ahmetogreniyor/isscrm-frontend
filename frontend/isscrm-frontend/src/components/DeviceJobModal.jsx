// src/components/DeviceJobModal.jsx
import React, { useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  MenuItem,
  Snackbar,
  Alert,
} from "@mui/material";
import { createJob } from "../api/deviceApi";

const DeviceJobModal = ({ open, onClose, device, refresh }) => {
  const [jobType, setJobType] = useState("CHANGE_WIFI_PSK");
  const [payload, setPayload] = useState({ ssid: "", psk: "" });
  const [loading, setLoading] = useState(false);
  const [toast, setToast] = useState({ open: false, message: "", severity: "success" });

  const handleSubmit = async () => {
    if (!device) return;
    setLoading(true);
    try {
      const body = {
        jobType,
        payload: JSON.stringify(payload),
      };
      await createJob(device.id, body);
      setToast({ open: true, message: "İşlem başarıyla oluşturuldu!", severity: "success" });
      refresh();
      onClose();
    } catch (err) {
      setToast({ open: true, message: "İşlem oluşturulamadı!", severity: "error" });
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Dialog open={open} onClose={onClose} fullWidth>
        <DialogTitle>Yeni İşlem – {device?.name}</DialogTitle>
        <DialogContent>
          <TextField
            select
            label="İşlem Türü"
            fullWidth
            margin="dense"
            value={jobType}
            onChange={(e) => setJobType(e.target.value)}
          >
            <MenuItem value="CHANGE_WIFI_PSK">Wi-Fi Şifresi Değiştir</MenuItem>
            <MenuItem value="REBOOT">Cihazı Yeniden Başlat</MenuItem>
            <MenuItem value="UPDATE_SPEED">Hız Limitini Güncelle</MenuItem>
          </TextField>

          {jobType === "CHANGE_WIFI_PSK" && (
            <>
              <TextField
                label="SSID"
                fullWidth
                margin="dense"
                value={payload.ssid}
                onChange={(e) => setPayload({ ...payload, ssid: e.target.value })}
              />
              <TextField
                label="Yeni Şifre"
                type="password"
                fullWidth
                margin="dense"
                value={payload.psk}
                onChange={(e) => setPayload({ ...payload, psk: e.target.value })}
              />
            </>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={onClose}>İptal</Button>
          <Button
            variant="contained"
            color="primary"
            onClick={handleSubmit}
            disabled={loading}
          >
            {loading ? "Gönderiliyor..." : "Gönder"}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Toast Bildirim */}
      <Snackbar
        open={toast.open}
        autoHideDuration={3000}
        onClose={() => setToast({ ...toast, open: false })}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
      >
        <Alert
          onClose={() => setToast({ ...toast, open: false })}
          severity={toast.severity}
          sx={{ width: "100%" }}
        >
          {toast.message}
        </Alert>
      </Snackbar>
    </>
  );
};

export default DeviceJobModal;
