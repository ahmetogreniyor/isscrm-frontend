import React, { useEffect, useState } from "react";
import {
  Box,
  Typography,
  Button,
  Card,
  CardContent,
  TextField,
  Grid,
  CircularProgress,
  Alert,
  Snackbar,
  Chip
} from "@mui/material";
import axios from "axios";

const API_URL = "http://localhost:8080/api/invoices";

const EInvoice = () => {
  const [invoices, setInvoices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({ customerName: "", amount: "" });
  const [toast, setToast] = useState({ open: false, message: "", severity: "success" });

  useEffect(() => {
    loadInvoices();
  }, []);

  const loadInvoices = async () => {
    setLoading(true);
    const res = await axios.get(API_URL);
    setInvoices(res.data);
    setLoading(false);
  };

  const handleGenerate = async () => {
    if (!form.customerName || !form.amount) {
      setToast({ open: true, message: "Tüm alanları doldurun!", severity: "warning" });
      return;
    }
    try {
      await axios.post(`${API_URL}/generate`, null, { params: form });
      setToast({ open: true, message: "Fatura oluşturuldu!", severity: "success" });
      setForm({ customerName: "", amount: "" });
      loadInvoices();
    } catch {
      setToast({ open: true, message: "Fatura oluşturulamadı!", severity: "error" });
    }
  };

  const handleStatusChange = async (id, newStatus) => {
    try {
      await axios.put(`${API_URL}/${id}/status`, null, { params: { status: newStatus } });
      setToast({ open: true, message: `Durum güncellendi: ${newStatus}`, severity: "info" });
      loadInvoices();
    } catch {
      setToast({ open: true, message: "Durum değiştirilemedi!", severity: "error" });
    }
  };

  return (
    <Box p={3}>
      <Typography variant="h5" mb={3}>🧾 E-Fatura Yönetimi</Typography>

      <Grid container spacing={2}>
        {/* Sol taraf — Yeni fatura oluştur */}
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography variant="h6" mb={2}>Yeni Fatura Oluştur</Typography>
              <TextField
                label="Müşteri Adı"
                fullWidth
                margin="dense"
                value={form.customerName}
                onChange={(e) => setForm({ ...form, customerName: e.target.value })}
              />
              <TextField
                label="Tutar (₺)"
                type="number"
                fullWidth
                margin="dense"
                value={form.amount}
                onChange={(e) => setForm({ ...form, amount: e.target.value })}
              />
              <Button
                variant="contained"
                color="primary"
                fullWidth
                sx={{ mt: 2 }}
                onClick={handleGenerate}
              >
                Oluştur
              </Button>
            </CardContent>
          </Card>
        </Grid>

        {/* Sağ taraf — Fatura listesi */}
        <Grid item xs={12} md={8}>
          <Card>
            <CardContent>
              <Typography variant="h6" mb={2}>Fatura Listesi</Typography>
              {loading ? (
                <CircularProgress />
              ) : invoices.length === 0 ? (
                <Typography color="text.secondary">Henüz fatura bulunmuyor.</Typography>
              ) : (
                invoices.map((i) => (
                  <Box key={i.id} borderBottom="1px solid #eee" py={1}>
                    <Box display="flex" justifyContent="space-between" alignItems="center">
                      <Typography variant="subtitle2">{i.invoiceNo}</Typography>
                      <Chip
                        label={i.status}
                        color={
                          i.status === "APPROVED"
                            ? "success"
                            : i.status === "SENT"
                            ? "info"
                            : "warning"
                        }
                        size="small"
                      />
                    </Box>

                    <Typography variant="body2" color="text.secondary">
                      {i.customerName} — ₺{i.amount}
                    </Typography>

                    <Box mt={1} display="flex" gap={1} flexWrap="wrap">
                      {/* XML indir */}
                      <Button
                        size="small"
                        variant="outlined"
                        onClick={() =>
                          window.open(`${API_URL}/${i.id}/download`, "_blank")
                        }
                      >
                        XML İndir
                      </Button>

                      {/* PDF indir */}
                      <Button
                        size="small"
                        variant="outlined"
                        color="secondary"
                        onClick={() =>
                          window.open(`${API_URL}/${i.id}/pdf`, "_blank")
                        }
                      >
                        PDF İndir
                      </Button>

                      {/* Durum değiştir */}
                      {i.status === "PENDING" && (
                        <Button
                          size="small"
                          variant="contained"
                          color="info"
                          onClick={() => handleStatusChange(i.id, "SENT")}
                        >
                          Faturayı Gönder
                        </Button>
                      )}

                      {i.status === "SENT" && (
                        <Button
                          size="small"
                          variant="contained"
                          color="success"
                          onClick={() => handleStatusChange(i.id, "APPROVED")}
                        >
                          Onayla
                        </Button>
                      )}
                    </Box>
                  </Box>
                ))
              )}
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Bildirim */}
      <Snackbar
        open={toast.open}
        autoHideDuration={3000}
        onClose={() => setToast({ ...toast, open: false })}
      >
        <Alert severity={toast.severity}>{toast.message}</Alert>
      </Snackbar>
    </Box>
  );
};

export default EInvoice;
