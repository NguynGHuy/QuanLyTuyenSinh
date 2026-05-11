import { initRouter } from "./core/router.js";
import { store } from "./core/store.js";

const THEME_LIGHT = "sky";
const THEME_DARK = "dark";

function updateThemeToggle(button, theme) {
  if (!button) {
    return;
  }
  const isDark = theme === THEME_DARK;
  button.classList.toggle("is-dark", isDark);
  button.setAttribute("aria-pressed", isDark ? "true" : "false");
  button.setAttribute("aria-label", isDark ? "Switch to light mode" : "Switch to dark mode");
  const label = button.querySelector(".theme-toggle-label");
  if (label) {
    label.textContent = isDark ? "Dark" : "Light";
  }
}

document.addEventListener("DOMContentLoaded", () => {
  const state = store.getState();
  const initialTheme = state.theme || THEME_LIGHT;
  let themeTimer = null;

  const getToggleButton = () => document.getElementById("btnThemeToggle");

  const applyTheme = (theme) => {
    document.documentElement.dataset.theme = theme;
    document.body.dataset.theme = theme;
    updateThemeToggle(getToggleButton(), theme);
  };

  const handleToggle = () => {
    const currentTheme = store.getState().theme || THEME_LIGHT;
    const nextTheme = currentTheme === THEME_DARK ? THEME_LIGHT : THEME_DARK;
    document.body.classList.add("theme-animate");
    if (themeTimer) {
      window.clearTimeout(themeTimer);
    }
    themeTimer = window.setTimeout(() => {
      document.body.classList.remove("theme-animate");
    }, 420);
    store.setState({ theme: nextTheme });
    applyTheme(nextTheme);
  };

  applyTheme(initialTheme);

  document.addEventListener("click", (event) => {
    const target = event.target.closest("#btnThemeToggle");
    if (!target) {
      return;
    }
    handleToggle();
  });

  // keep body theme in sync with store
  store.subscribe((s) => {
    if (s && s.theme) {
      applyTheme(s.theme);
    }
  });

  const hasSpaShell = Boolean(document.getElementById("app-main") && document.getElementById("app-header"));
  if (hasSpaShell) {
    initRouter();
  }
});
