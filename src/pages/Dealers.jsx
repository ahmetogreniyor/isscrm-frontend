import React, { useState, useEffect } from "react";
import { DataGrid } from "@mui/x-data-grid";
import {
  Box,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";
import { Formik, Form, Field } from "formik";
import { TextField as FormikTextField } from "formik-mui";
import * as Yup from "yup";
import api from "../api";

const DealerSchema = Yup.object().shape({
  dealerCode: Yup.string().required("Dealer code is required"),
  dealerName: Yup.string().required("Dealer name is required"),
  category: Yup.string().required("Category is required"),
  creditLimit: Yup.number().required("Credit limit is required").min(0),
});

const Dealers = () => {
  const [dealers, setDealers] = useState([]);
  const [open, setOpen] = useState(false);

  useEffect(() => {
    api
      .get("/api/dealers")
      .then((res) => setDealers(res.data))
      .catch((err) => console.error("Dealer data fetch error:", err));
  }, []);

  const handleAddDealer = (values, { resetForm }) => {
    api
      .post("/api/dealers", values)
      .then(() => {
        setOpen(false);
        resetForm();
        api.get("/api/dealers").then((res) => setDealers(res.data));
      })
      .catch((err) => console.error("Dealer add error:", err));
  };

  const columns = [
    { field: "id", headerName: "ID", width: 70 },
    { field: "dealerCode", headerName: "Code", width: 120 },
    { field: "dealerName", headerName: "Name", width: 200 },
    { field: "category", headerName: "Category", width: 120 },
    { field: "creditLimit", headerName: "Credit Limit", width: 150 },
    { field: "balance", headerName: "Balance", width: 150 },
    { field: "createdAt", headerName: "Created At", width: 200 },
  ];

  return (
    <Box sx={{ p: 2 }}>
      <Button variant="contained" onClick={() => setOpen(true)} sx={{ mb: 2 }}>
        Add Dealer
      </Button>

      <DataGrid rows={dealers} columns={columns} pageSize={5} autoHeight />

      <Dialog open={open} onClose={() => setOpen(false)} fullWidth maxWidth="sm">
        <DialogTitle>Add New Dealer</DialogTitle>
        <Formik
          initialValues={{
            dealerCode: "",
            dealerName: "",
            category: "",
            creditLimit: "",
          }}
          validationSchema={DealerSchema}
          onSubmit={handleAddDealer}
        >
          {({ isSubmitting }) => (
            <Form>
              <DialogContent>
                <Field
                  component={FormikTextField}
                  name="dealerCode"
                  label="Dealer Code"
                  fullWidth
                  margin="normal"
                />
                <Field
                  component={FormikTextField}
                  name="dealerName"
                  label="Dealer Name"
                  fullWidth
                  margin="normal"
                />
                <Field
                  component={FormikTextField}
                  name="category"
                  label="Category"
                  fullWidth
                  margin="normal"
                />
                <Field
                  component={FormikTextField}
                  name="creditLimit"
                  label="Credit Limit"
                  type="number"
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

export default Dealers;
