import React, { useState, useEffect } from "react";
import { DataGrid } from "@mui/x-data-grid";
import {
  Box,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  CircularProgress,
} from "@mui/material";
import { Formik, Form, Field } from "formik";
import { TextField as FormikTextField } from "formik-mui";
import * as Yup from "yup";
import api from "../api"; // ✅ Token’lı axios instance

// ✅ Validation Schema
const CustomerSchema = Yup.object().shape({
  customerCode: Yup.string().required("Customer code is required"),
  customerName: Yup.string().required("Customer name is required"),
  email: Yup.string().email("Invalid email").required("Email is required"),
  phone: Yup.string().required("Phone number is required"),
});

const Customers = () => {
  const [customers, setCustomers] = useState([]);
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(true);

  // ✅ Backend'den müşteri listesini al
  const fetchCustomers = () => {
    setLoading(true);
    api
      .get("/api/customers")
      .then((res) => setCustomers(res.data))
      .catch((err) => console.error("Customer data fetch error:", err))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchCustomers();
  }, []);

  // ✅ Yeni müşteri ekleme
  const handleAddCustomer = (values, { resetForm }) => {
    api
      .post("/api/customers", values)
      .then(() => {
        setOpen(false);
        resetForm();
        fetchCustomers();
      })
      .catch((err) => console.error("Customer add error:", err));
  };

  // ✅ Kolon tanımları
  const columns = [
    { field: "id", headerName: "ID", width: 70 },
    { field: "customerCode", headerName: "Code", width: 120 },
    { field: "customerName", headerName: "Name", width: 200 },
    { field: "email", headerName: "Email", width: 200 },
    { field: "phone", headerName: "Phone", width: 150 },
    {
      field: "createdAt",
      headerName: "Created At",
      width: 200,
      valueGetter: (params) => params.row?.createdAt ?? "",
    },
  ];

  if (loading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 10 }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ p: 2 }}>
      <Button variant="contained" onClick={() => setOpen(true)} sx={{ mb: 2 }}>
        Add Customer
      </Button>

      <DataGrid rows={customers} columns={columns} pageSize={5} autoHeight />

      <Dialog open={open} onClose={() => setOpen(false)} fullWidth maxWidth="sm">
        <DialogTitle>Add New Customer</DialogTitle>
        <Formik
          initialValues={{
            customerCode: "",
            customerName: "",
            email: "",
            phone: "",
          }}
          validationSchema={CustomerSchema}
          onSubmit={handleAddCustomer}
        >
          {({ isSubmitting }) => (
            <Form>
              <DialogContent>
                <Field
                  component={FormikTextField}
                  name="customerCode"
                  label="Customer Code"
                  fullWidth
                  margin="normal"
                />
                <Field
                  component={FormikTextField}
                  name="customerName"
                  label="Customer Name"
                  fullWidth
                  margin="normal"
                />
                <Field
                  component={FormikTextField}
                  name="email"
                  label="Email"
                  fullWidth
                  margin="normal"
                />
                <Field
                  component={FormikTextField}
                  name="phone"
                  label="Phone"
                  fullWidth
                  margin="normal"
                />
              </DialogContent>
              <DialogActions>
                <Button onClick={() => setOpen(false)}>Cancel</Button>
                <Button type="submit" variant="contained" disabled={isSubmitting}>
                  Save
                </Button>
              </DialogActions>
            </Form>
          )}
        </Formik>
      </Dialog>
    </Box>
  );
};

export default Customers;
