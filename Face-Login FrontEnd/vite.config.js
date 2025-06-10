import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vitejs.dev/config/
export default defineConfig(({ command }) => {
  const config = {
    plugins: [react()],
    base: "/",
    server: {
      open: false, // Automatically open the browser when the server starts
    },
  };

  return config;
});
