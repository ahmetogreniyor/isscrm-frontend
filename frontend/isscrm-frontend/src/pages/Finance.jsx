import React, { useEffect, useState } from "react";
import {
  Box,
  Typography,
  TextField,
  Button,
  Card,
  CardContent,
  Grid,
  Divider,
  Snackbar,
  Alert,
  CircularProgress,
} from "@mui/material";
import axios from "axios";

const API_URL = "http://localhost:8080/api/payments";

const Finance = () => {
  const [payments, setPayments] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [newPayment, setNewPayment] = useState({
    customerName: "",
    amount: "",
    method: "Nakit",
    description: "",
  });
  const [toast, setToast] = useState({ open: false, message: "", severity: "success" });

  useEffect(() => {
    loadTodayPayments();
  }, []);

  const loadTodayPayments = async () => {
    setLoading(true);
    const res = await axios.get(`${API_URL}/today`);
    setPayments(res.data.payments);
    setTotal(res.data.total);
    setLoading(false);
  };

  const handleAddPayment = async () => {
    try {
      await axios.post(API_URL, newPayment);
      setToast({ open: true, message: "Tahsilat eklendi!", severity: "success" });
      setNewPayment({ customerName: "", amount: "", method: "Nakit", description: "" });
      loadTodayPayments();
    } catch {
      setToast({ open: true, message: "Tahsilat eklenemedi!", severity: "error" });
    }
  };

  return (
    <Box p={3}>
      <Typography variant="h5" mb={3}>
        💰 Günlük Tahsilatlar & Kasa Yönetimi
      </Typography>

      <Grid container spacing={2}>
        <Grid item xs={12} md={5}>
          <Card>
            <CardContent>
              <Typography variant="h6" mb={2}>
                Yeni Tahsilat Ekle
              </Typography>
              <TextField
                label="Müşteri Adı"
                fullWidth
                margin="dense"
                value={newPayment.customerName}
                onChange={(e) => setNewPayment({ ...newPayment, customerName: e.target.value })}
              />
              <TextField
                label="Tutar (₺)"
                type="number"
                fullWidth
                margin="dense"
                value={newPayment.amount}
                onChange={(e) => setNewPayment({ ...newPayment, amount: e.target.value })}
              />
              <TextField
                label="Yöntem"
                fullWidth
                margin="dense"
                value={newPayment.method}
                onChange={(e) => setNewPayment({ ...newPayment, method: e.target.value })}
              />
              <TextField
                label="Açıklama"
                fullWidth
                margin="dense"
                value={newPayment.description}
                onChange={(e) => setNewPayment({ ...newPayment, description: e.target.value })}
              />
              <Button variant="contained" color="primary" fullWidth sx={{ mt: 2 }} onClick={handleAddPayment}>
                Kaydet
              </Button>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} md={7}>
          <Card>
            <CardContent>
              <Typography variant="h6" mb={2}>
                Günlük Kasa Özeti
              </Typography>
              {loading ? (
                <CircularProgress />
              ) : (
                <>
                  <Typography variant="h5" color="green" mb={2}>
                    Toplam: ₺{total.toFixed(2)}
                  </Typography>
                  <Divider />
                  {payments.length === 0 ? (
                    <Typography color="text.secondary" mt={2}>
                      Bugün için kayıtlı tahsilat bulunamadı.
                    </Typography>
                  ) : (
                    payments.map((p) => (
                      <Box key={p.id} borderBottom="1px solid #eee" py={1}>
                        <Typography variant="subtitle2">{p.customerName}</Typography>
                        <Typography variant="body2" color="text.secondary">
                          {p.method} – ₺{p.amount} – {p.description}
                        </Typography>
                      </Box>
                    ))
                  )}
                </>
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

export default Finance;
