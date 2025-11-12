import React, { useEffect, useState } from "react";
import {
  Box,
  Typography,
  CircularProgress,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Stack,
  Snackbar,
  Alert,
} from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import axios from "axios";
import { Formik, Form, Field } from "formik";
import { TextField } from "formik-mui";
import * as Yup from "yup";

const Tariffs = () => {
  const [tariffs, setTariffs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [openDialog, setOpenDialog] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [selectedTariff, setSelectedTariff] = useState(null);
  const [snackbar, setSnackbar] = useState({ open: false, message: "", severity: "success" });

  // 🔄 Fetch Tariffs
  const fetchTariffs = () => {
    setLoading(true);
    axios
      .get("http://localhost:8080/api/tariffs")
      .then((res) => setTariffs(res.data))
      .catch((err) => console.error("Tariff fetch error:", err))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchTariffs();
  }, []);

  // ✅ Validation
  const validationSchema = Yup.object({
    name: Yup.string().required("Tariff name required"),
    speedMbps: Yup.number().required("Speed required"),
    price: Yup.number().required("Price required"),
    category: Yup.string().required("Category required"),
  });

  const handleCloseSnackbar = () => setSnackbar({ ...snackbar, open: false });

  // 🟢 Add/Edit Tariff
  const handleSubmit = (values, { setSubmitting, resetForm }) => {
    const apiUrl = editMode
      ? `http://localhost:8080/api/tariffs/${selectedTariff.id}`
      : "http://localhost:8080/api/tariffs";
    const method = editMode ? axios.put : axios.post;

    method(apiUrl, values)
      .then(() => {
        setSnackbar({
          open: true,
          message: editMode ? "Tariff updated successfully!" : "Tariff added successfully!",
          severity: "success",
        });
        fetchTariffs();
        resetForm();
        setOpenDialog(false);
      })
      .catch(() => {
        setSnackbar({ open: true, message: "Error saving tariff.", severity: "error" });
      })
      .finally(() => setSubmitting(false));
  };

  // ➕ Add Tariff
  const handleAdd = () => {
    setSelectedTariff(null);
    setEditMode(false);
    setOpenDialog(true);
  };

  // ✏️ Edit Tariff
  const handleEdit = (tariff) => {
    setSelectedTariff(tariff);
    setEditMode(true);
    setOpenDialog(true);
  };

  // 🗑️ Delete Tariff
  const handleDelete = (id) => {
    if (!window.confirm("Are you sure you want to delete this tariff?")) return;
    axios
      .delete(`http://localhost:8080/api/tariffs/${id}`)
      .then(() => {
        setSnackbar({ open: true, message: "Tariff deleted!", severity: "success" });
        fetchTariffs();
      })
      .catch(() => {
        setSnackbar({ open: true, message: "Failed to delete tariff.", severity: "error" });
      });
  };

  // 📋 Columns
  const columns = [
    { field: "id", headerName: "ID", width: 60 },
    { field: "name", headerName: "Tariff Name", flex: 1.5 },
    { field: "speedMbps", headerName: "Speed (Mbps)", flex: 1 },
    { field: "price", headerName: "Price ($)", flex: 1 },
    { field: "category", headerName: "Category", flex: 1 },
    {
      field: "actions",
      headerName: "Actions",
      flex: 1.2,
      renderCell: (params) => (
        <Stack direction="row" spacing={1}>
          <Button
            variant="outlined"
            color="primary"
            size="small"
            onClick={() => handleEdit(params.row)}
          >
            Edit
          </Button>
          <Button
            variant="outlined"
            color="error"
            size="small"
            onClick={() => handleDelete(params.row.id)}
          >
            Delete
          </Button>
        </Stack>
      ),
    },
  ];

  return (
    <Box p={4}>
      <Typography variant="h4" mb={3} fontWeight="bold">
        Tariff Management
      </Typography>

      <Button variant="contained" color="primary" onClick={handleAdd} sx={{ mb: 2 }}>
        + Add Tariff
      </Button>

      {loading ? (
        <CircularProgress />
      ) : (
        <DataGrid
          rows={tariffs}
          columns={columns}
          getRowId={(row) => row.id}
          pageSize={5}
          autoHeight
          disableSelectionOnClick
          sx={{ boxShadow: 3, borderRadius: 2, backgroundColor: "#fff" }}
        />
      )}

      {/* ➕ Add/Edit Tariff Dialog */}
      <Dialog open={openDialog} onClose={() => setOpenDialog(false)} fullWidth>
        <DialogTitle>{editMode ? "Edit Tariff" : "Add New Tariff"}</DialogTitle>

        <Formik
          initialValues={
            selectedTariff || {
              name: "",
              speedMbps: "",
              price: "",
              quotaGb: 0,
              category: "",
            }
          }
          validationSchema={validationSchema}
          onSubmit={handleSubmit}
          enableReinitialize
        >
          {({ isSubmitting }) => (
            <Form>
              <DialogContent>
                <Stack spacing={2} mt={1}>
                  <Field component={TextField} name="name" label="Tariff Name" fullWidth />
                  <Field
                    component={TextField}
                    name="speedMbps"
                    label="Speed (Mbps)"
                    type="number"
                    fullWidth
                  />
                  <Field
                    component={TextField}
                    name="price"
                    label="Price ($)"
                    type="number"
                    fullWidth
                  />
                  <Field
                    component={TextField}
                    name="quotaGb"
                    label="Quota (GB)"
                    type="number"
                    fullWidth
                  />
                  <Field component={TextField} name="category" label="Category" fullWidth />
                </Stack>
              </DialogContent>

              <DialogActions>
                <Button onClick={() => setOpenDialog(false)}>Cancel</Button>
                <Button type="submit" variant="contained" color="primary" disabled={isSubmitting}>
                  {editMode ? "Save Changes" : "Save Tariff"}
                </Button>
              </DialogActions>
            </Form>
          )}
        </Formik>
      </Dialog>

      <Snackbar
        open={snackbar.open}
        autoHideDuration={2500}
        onClose={handleCloseSnackbar}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
      >
        <Alert severity={snackbar.severity} variant="filled">
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default Tariffs;
