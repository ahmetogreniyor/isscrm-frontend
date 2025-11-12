import React, { useEffect, useState } from "react";
import {
  Box,
  Typography,
  CircularProgress,
  Dialog,
  DialogTitle,
  DialogContent,
  IconButton,
  Tooltip,
  Chip,
  Snackbar,
  Alert,
} from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import AddTaskIcon from "@mui/icons-material/AddTask";
import HistoryIcon from "@mui/icons-material/History";
import { getDevices, getJobsByDevice } from "../api/deviceApi";
import { connectSocket, disconnectSocket } from "../api/socket";
import DeviceJobModal from "../components/DeviceJobModal";

const Device = () => {
  const [devices, setDevices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedDevice, setSelectedDevice] = useState(null);
  const [jobs, setJobs] = useState([]);
  const [jobModalOpen, setJobModalOpen] = useState(false);
  const [jobHistoryOpen, setJobHistoryOpen] = useState(false);
  const [toast, setToast] = useState({ open: false, message: "", severity: "info" });

  useEffect(() => {
    loadDevices();
    connectSocket(handleJobUpdate);
    return () => disconnectSocket();
  }, []);

  const loadDevices = async () => {
    setLoading(true);
    const data = await getDevices();
    setDevices(data);
    setLoading(false);
  };

  // 🔔 Backend’den job güncellemesi geldiğinde tetiklenir
  const handleJobUpdate = (updatedJob) => {
    setToast({
      open: true,
      message: `Job güncellendi: ${updatedJob.jobType} → ${updatedJob.status}`,
      severity: updatedJob.status === "SUCCESS" ? "success" : "warning",
    });

    // Eğer açık job geçmişi popup varsa otomatik yenile
    if (selectedDevice && updatedJob.device?.id === selectedDevice.id) {
      getJobsByDevice(selectedDevice.id).then(setJobs);
    }
  };

  const openJobModal = (device) => {
    setSelectedDevice(device);
    setJobModalOpen(true);
  };

  const openJobHistory = async (device) => {
    setSelectedDevice(device);
    const data = await getJobsByDevice(device.id);
    setJobs(data);
    setJobHistoryOpen(true);
  };

  const columns = [
    { field: "id", headerName: "ID", width: 70 },
    { field: "name", headerName: "Cihaz Adı", flex: 1 },
    { field: "vendor", headerName: "Marka", width: 120 },
    { field: "model", headerName: "Model", width: 150 },
    { field: "ipAddress", headerName: "IP Adresi", width: 150 },
    { field: "region", headerName: "Bölge", width: 120 },
    {
      field: "status",
      headerName: "Durum",
      width: 130,
      renderCell: (params) => (
        <Chip
          label={params.row.status}
          color={params.row.status === "ONLINE" ? "success" : "error"}
          size="small"
        />
      ),
    },
    {
      field: "actions",
      headerName: "İşlemler",
      width: 180,
      renderCell: (params) => (
        <>
          <Tooltip title="Yeni İşlem (Job)">
            <IconButton color="primary" onClick={() => openJobModal(params.row)}>
              <AddTaskIcon />
            </IconButton>
          </Tooltip>
          <Tooltip title="İşlem Geçmişi">
            <IconButton color="secondary" onClick={() => openJobHistory(params.row)}>
              <HistoryIcon />
            </IconButton>
          </Tooltip>
        </>
      ),
    },
  ];

  return (
    <Box p={3}>
      <Typography variant="h5" mb={2}>
        🖥️ Cihaz Yönetimi (Canlı İzleme Aktif)
      </Typography>

      {loading ? (
        <CircularProgress />
      ) : (
        <DataGrid
          rows={devices}
          columns={columns}
          pageSize={10}
          autoHeight
          disableRowSelectionOnClick
        />
      )}

      {/* Modal */}
      <DeviceJobModal
        open={jobModalOpen}
        onClose={() => setJobModalOpen(false)}
        device={selectedDevice}
        refresh={loadDevices}
      />

      {/* Job Geçmişi */}
      <Dialog open={jobHistoryOpen} onClose={() => setJobHistoryOpen(false)} fullWidth maxWidth="md">
        <DialogTitle>{selectedDevice?.name} – İşlem Geçmişi</DialogTitle>
        <DialogContent>
          {jobs.length === 0 ? (
            <Typography color="text.secondary">Kayıt bulunamadı</Typography>
          ) : (
            jobs
              .slice()
              .reverse()
              .map((job) => (
                <Box key={job.id} p={1} borderBottom="1px solid #ddd">
                  <Typography variant="subtitle2">{job.jobType}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    Durum:{" "}
                    <b
                      style={{
                        color:
                          job.status === "SUCCESS"
                            ? "green"
                            : job.status === "FAILED"
                            ? "red"
                            : "#999",
                      }}
                    >
                      {job.status}
                    </b>
                  </Typography>
                  <Typography variant="body2">{job.resultLog}</Typography>
                </Box>
              ))
          )}
        </DialogContent>
      </Dialog>

      {/* 🔔 Bildirim */}
      <Snackbar
        open={toast.open}
        autoHideDuration={3000}
        onClose={() => setToast({ ...toast, open: false })}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
      >
        <Alert severity={toast.severity}>{toast.message}</Alert>
      </Snackbar>
    </Box>
  );
};

export default Device;
