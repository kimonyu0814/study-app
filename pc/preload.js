// 화면(웹페이지)에서 쓰는 앱 기능 — 휴대폰 앱의 App 과 같은 모양
"use strict";
const { contextBridge, ipcRenderer } = require("electron");

contextBridge.exposeInMainWorld("App", {
  role: () => ipcRenderer.sendSync("role"),
  pw: () => ipcRenderer.sendSync("pw"),
  prefs: () => ipcRenderer.sendSync("prefs"),
  setPref: (k, v) => ipcRenderer.send("setPref", k, !!v),
  version: () => "PC " + ipcRenderer.sendSync("version"),
  resetRole: () => ipcRenderer.send("reset"),
  isPC: true
});
// 바깥 틀(shell.html)에서만 쓰는 기능
contextBridge.exposeInMainWorld("Shell", {
  login: (role, pw) => ipcRenderer.invoke("login", role, pw),
  changes: () => ipcRenderer.invoke("changes"),
  getTab: () => ipcRenderer.sendSync("getTab"),
  setTab: k => ipcRenderer.send("tab", k),
  autostart: () => ipcRenderer.sendSync("autostart"),
  setAutostart: v => ipcRenderer.send("setAutostart", !!v),
  onOpenTab: cb => ipcRenderer.on("open-tab", (e, k) => cb(k))
});
