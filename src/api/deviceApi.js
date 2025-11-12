// src/api/deviceApi.js
import axios from "axios";

const API_URL = "http://localhost:8080/api/devices";

// ✅ Cihaz listesi
export const getDevices = async () => {
  try {
    const res = await axios.get(API_URL);
    return res.data;
  } catch (err) {
    console.error("Cihaz listesi alınamadı:", err);
    return [];
  }
};

// ✅ Seçili cihazın job geçmişi
export const getJobsByDevice = async (deviceId) => {
  try {
    const res = await axios.get(`${API_URL}/${deviceId}/jobs`);
    return res.data;
  } catch (err) {
    console.error("Job geçmişi alınamadı:", err);
    return [];
  }
};

// ✅ Yeni job oluştur
export const createJob = async (deviceId, job) => {
  try {
    const res = await axios.post(`${API_URL}/${deviceId}/jobs`, job);
    return res.data;
  } catch (err) {
    console.error("Job oluşturulamadı:", err);
    throw err;
  }
};
