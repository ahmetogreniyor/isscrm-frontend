import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  // ✅ Tarayıcı ortamında global değişkenini window olarak tanımlıyoruz
  define: {
    global: "window",
  },
  server: {
    port: 5173, // kendi portun farklıysa değiştirebilirsin
  },
});
