import React, { createContext, useState, useMemo, useEffect } from "react";
import { createTheme, ThemeProvider, CssBaseline } from "@mui/material";

export const ThemeContext = createContext();

const CustomThemeProvider = ({ children }) => {
  const [mode, setMode] = useState(localStorage.getItem("themeMode") || "light");

  const theme = useMemo(
    () =>
      createTheme({
        palette: {
          mode,
          ...(mode === "light"
            ? {
                background: { default: "#f5f5f5", paper: "#fff" },
                primary: { main: "#1976d2" },
              }
            : {
                background: { default: "#121212", paper: "#1e1e1e" },
                primary: { main: "#90caf9" },
              }),
        },
        shape: { borderRadius: 10 },
      }),
    [mode]
  );

  useEffect(() => {
    localStorage.setItem("themeMode", mode);
  }, [mode]);

  const toggleTheme = () => setMode((prev) => (prev === "light" ? "dark" : "light"));

  return (
    <ThemeContext.Provider value={{ mode, toggleTheme }}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        {children}
      </ThemeProvider>
    </ThemeContext.Provider>
  );
};

export default CustomThemeProvider;
