const state = {
  session: null,
  route: null,
  theme: (typeof localStorage !== "undefined" && localStorage.getItem("theme")) || "sky",
};

const listeners = new Set();

function getState() {
  return {
    ...state,
    session: state.session ? { ...state.session } : null,
  };
}

function setState(partial) {
  Object.assign(state, partial);
  if (partial && Object.prototype.hasOwnProperty.call(partial, "theme")) {
    try {
      localStorage.setItem("theme", state.theme);
    } catch (e) {
      // ignore
    }
  }
  const snapshot = getState();
  listeners.forEach((listener) => listener(snapshot));
}

function subscribe(listener) {
  listeners.add(listener);
  return () => listeners.delete(listener);
}

export const store = {
  getState,
  setState,
  subscribe,
};
