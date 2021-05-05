import { now } from "svelte/internal";
import { writable, derived } from "svelte/store";

export type Severity = "INFO" | "WARN" | "ERR";

export type Notification = {
  id: string;
  text: string;
  active: boolean;
  severity: Severity;
};

export const notifications = (() => {
  const emptyNotifications = (): Notification[] => [];
  const { subscribe, update } = writable(emptyNotifications());

  const deactivate = (id: string) => {
    update((notifications) => {
      return notifications.map((n) => {
        if (n.id === id) {
          return { ...n, active: false };
        } else {
          return n;
        }
      });
    });
  };

  const addError = (text: string) => {
    const id = "" + now() + Math.random();
    update((n) => [{ active: true, text, severity: "ERR", id }, ...n]);
    setTimeout(deactivate, 5000, id);
  };

  return {
    subscribe,
    addError,
    deactivate,
  };
})();

export const activeNotifications = derived(notifications, ($notifications) => $notifications.filter((n) => n.active));
