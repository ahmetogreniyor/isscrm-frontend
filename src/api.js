import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080", // ✅ Backend adresin
});

// Her isteğe otomatik token ekle
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  } else {
    console.warn("⚠️ No token found in localStorage");
  }
  return config;
});

export default api;
