import { initRouter } from "./core/router.js";
import { store } from "./core/store.js";

document.addEventListener("DOMContentLoaded", () => {
  const state = store.getState();
  document.body.dataset.theme = state.theme || "sky";
  // keep body theme in sync with store
  store.subscribe((s) => {
    if (s && s.theme) {
      document.body.dataset.theme = s.theme;
    }
  });

  initRouter();
});
