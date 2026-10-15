// 학습기록 PC 앱 (윈도우)
"use strict";
const { app, BrowserWindow, Tray, Menu, Notification, ipcMain, shell, session, nativeImage, dialog } = require("electron");
const path = require("path");
const fs = require("fs");
const L = require("./logic.js");

const API = "https://script.google.com/macros/s/AKfycbzwUGDOfoBCJbPQx96HCMN8vin0OPCcREB-kYGv-yiDVGkHIOlapcL85oasQRA71NdX/exec";
const PAGES = "https://kimonyu0814.github.io/study-app/";
const PEOPLE = { "0000": "온유", "0001": "엄마", "0002": "아빠", "0003": "태은" };
const PREFS = {
  "0000": ["n_am", "n_pm", "n_ask", "n_msg"],
  "0001": ["n_dinner", "n_home", "n_msg"],
  "0002": ["n_pickup", "n_home", "n_msg"],
  "0003": ["n_class", "n_study", "n_done", "n_msg"]
};

app.setAppUserModelId("com.onyu.study.pc");
if (!app.requestSingleInstanceLock()) { app.quit(); process.exit(0); }

/* ---------- 설정 저장 (이 PC 에만) ---------- */
const CFG = () => path.join(app.getPath("userData"), "config.json");
let cfg = { role: "", pw: "", prefs: {}, seenAt: "", fired: {}, tab: "" };
try { cfg = Object.assign(cfg, JSON.parse(fs.readFileSync(CFG(), "utf8"))); } catch (e) {}
const save = () => { try { fs.writeFileSync(CFG(), JSON.stringify(cfg)); } catch (e) {} };
const on = k => cfg.prefs[k] !== false;

/* ---------- 창 · 트레이 ---------- */
let win = null, tray = null, quitting = false;
function createWindow(hidden) {
  win = new BrowserWindow({
    width: 1180, height: 860, minWidth: 420, minHeight: 560,
    title: "학습기록", backgroundColor: "#FBFAF7", show: !hidden,
    icon: path.join(__dirname, "build", "icon.png"),
    autoHideMenuBar: true,
    webPreferences: { preload: path.join(__dirname, "preload.js"), nodeIntegrationInSubFrames: true, contextIsolation: true, sandbox: false }
  });
  win.loadFile(path.join(__dirname, "shell.html"));
  win.on("close", e => { if (!quitting) { e.preventDefault(); win.hide(); } });   // 닫으면 트레이로
  win.webContents.setWindowOpenHandler(({ url }) => { shell.openExternal(url); return { action: "deny" }; });
}
function showWin(tab) {
  if (!win) return;
  win.show(); win.focus();
  if (tab) win.webContents.send("open-tab", tab);
}
function createTray() {
  tray = new Tray(nativeImage.createFromPath(path.join(__dirname, "build", "tray.png")));
  tray.setToolTip("학습기록");
  tray.setContextMenu(Menu.buildFromTemplate([
    { label: "열기", click: () => showWin() },
    { type: "separator" },
    { label: "종료", click: () => { quitting = true; app.quit(); } }
  ]));
  tray.on("click", () => showWin());
}
app.on("second-instance", () => showWin());

/* ---------- 화면에서 부르는 기능 ---------- */
ipcMain.on("role", e => { e.returnValue = cfg.role; });
ipcMain.on("pw", e => { e.returnValue = cfg.pw; });
ipcMain.on("version", e => { e.returnValue = app.getVersion(); });
ipcMain.on("prefs", e => {
  const o = {}; (PREFS[cfg.role] || []).forEach(k => { o[k] = on(k); }); e.returnValue = JSON.stringify(o);
});
ipcMain.on("setPref", (e, k, v) => { cfg.prefs[k] = !!v; save(); });
ipcMain.on("tab", (e, k) => { cfg.tab = k; save(); });
ipcMain.on("getTab", e => { e.returnValue = cfg.tab || ""; });
ipcMain.on("reset", () => {
  cfg.role = ""; cfg.pw = ""; cfg.seenAt = ""; save();
  if (win) win.loadFile(path.join(__dirname, "shell.html"));
});
ipcMain.handle("login", async (e, role, pw) => {
  if (!PEOPLE[role]) return { ok: false, error: "code" };
  if (role === "0000") {
    try {
      const r = await fetch(API, { method: "POST", headers: { "Content-Type": "text/plain;charset=utf-8" },
        body: JSON.stringify({ app: "family", action: "auth", from: "0000", pw }) });
      const j = await r.json();
      if (!j || !j.ok) return { ok: false, error: "pw" };
    } catch (err) { return { ok: false, error: "net" }; }
  }
  cfg.role = role; cfg.pw = role === "0000" ? pw : ""; cfg.seenAt = new Date().toISOString(); save();
  return { ok: true };
});
ipcMain.handle("changes", async () => {
  try {
    const arr = await (await fetch(PAGES + "changes.json?t=" + Date.now())).json();
    if (!Array.isArray(arr) || !arr.length) return null;
    const newest = arr[0].id, seen = cfg.seenChange || "";
    cfg.seenChange = newest; save();
    if (newest === seen) return null;
    const forMe = o => !o.who || o.who.includes(cfg.role);
    const parts = [];
    for (const c of arr) {
      if (c.id === seen) break;
      if (!forMe(c)) continue;
      const lines = (c.items || []).filter(forMe).map(i => "• " + i.t);
      if (lines.length) parts.push(c.title + "\n" + lines.join("\n"));
      if (!seen) break;
    }
    return parts.length ? parts.join("\n\n") : null;
  } catch (e) { return null; }
});
ipcMain.on("autostart", e => { e.returnValue = app.getLoginItemSettings().openAtLogin; });
ipcMain.on("setAutostart", (e, v) => { if (app.isPackaged) app.setLoginItemSettings({ openAtLogin: !!v, args: ["--hidden"] }); });

/* ---------- 알림 (앱이 켜져 있는 동안, 트레이 포함) ---------- */
function notify(title, body, tab) {
  if (!Notification.isSupported()) return;
  const n = new Notification({ title, body, icon: path.join(__dirname, "build", "icon.png") });
  n.on("click", () => showWin(tab));
  n.show();
}
async function getJSON(url) { const r = await fetch(url); return r.json(); }

let family = null;
async function pollFamily() {
  if (!cfg.role) return;
  try {
    const j = await getJSON(API + "?app=family&t=" + Date.now());
    if (!j || !j.data) return;
    family = j.data;
    const mine = (family.msgs || []).filter(m => m.to === cfg.role && m.at > (cfg.seenAt || ""));
    if (!cfg.seenAt) { cfg.seenAt = new Date().toISOString(); save(); return; }   // 처음엔 지난 메시지로 알림 안 함
    if (!mine.length) return;
    cfg.seenAt = mine[mine.length - 1].at; save();
    mine.slice(-5).forEach(m => {
      const kind = m.kind || "msg", key = "n_" + kind;
      if (!on((PREFS[cfg.role] || []).includes(key) ? key : "n_msg")) return;
      notify(`${PEOPLE[m.from] || m.from}의 메시지`, m.text, cfg.role === "0000" ? "admin" : "msg");
    });
  } catch (e) {}
}

/* 정해진 시각 알림: 그 시각부터 5분 안에 한 번 (PC 가 잠들었다 깨어나도 놓치지 않게) */
function due(at, now) { const d = minutes(now) - minutes(at); return d >= 0 && d < 5; }
function minutes(hm) { const [h, m] = hm.split(":").map(Number); return h * 60 + m; }
function fireOnce(key, fn) {
  const { date } = L.kst();
  if (cfg.fired[key] === date) return;
  cfg.fired[key] = date; save();
  fn();
}
async function tick() {
  if (!cfg.role) return;
  const { date, hm, wd } = L.kst();
  if (cfg.role === "0000") {
    for (const [k, at, pref] of [["am", "08:00", "n_am"], ["pm", "17:50", "n_pm"]]) {
      if (on(pref) && due(at, hm)) fireOnce(k, async () => {
        try { const j = await getJSON(API + "?app=exam&t=" + Date.now()); const s = L.studyNote(k, L.summarize(j.data)); notify(s.title, s.body, "study"); } catch (e) {}
      });
    }
    if (wd <= 4 && on("n_ask") && due("20:50", hm)) fireOnce("ask", () =>
      notify("밥 · 데리러 올 곳", "오후 9시에 엄마(밥) · 아빠(데리러)에게 각각 갑니다. 누르면 바로 답할 수 있습니다.", "admin"));
  }
  if (cfg.role === "0003") {
    L.taeNotes(family, date).forEach(n => {
      if (on(n.pref) && due(n.at, hm)) fireOnce("tae-" + n.at + "-" + n.title, () => notify(n.title, n.body, "schedule"));
    });
  }
}

/* ---------- 시작 ---------- */
app.whenReady().then(() => {
  // 채팅 녹음 · 알림 허용
  session.defaultSession.setPermissionRequestHandler((wc, perm, cb) => cb(["media", "notifications", "clipboard-sanitized-write"].includes(perm)));
  session.defaultSession.setPermissionCheckHandler((wc, perm) => ["media", "notifications"].includes(perm));
  createWindow(process.argv.includes("--hidden"));
  createTray();
  if (app.isPackaged && !cfg.autostartSet) {         // 처음 설치하면 PC 켤 때 자동 실행 (설정에서 끌 수 있음)
    app.setLoginItemSettings({ openAtLogin: true, args: ["--hidden"] });
    cfg.autostartSet = true; save();
  }
  pollFamily(); tick();
  setInterval(pollFamily, 30000);
  setInterval(tick, 30000);
  // 자동 업데이트 (GitHub 의 새 설치 파일)
  if (app.isPackaged) {
    try {
      const { autoUpdater } = require("electron-updater");
      autoUpdater.on("update-downloaded", info => {
        dialog.showMessageBox(win, { type: "info", buttons: ["지금 다시 시작", "나중에"], title: "업데이트",
          message: `새 버전(${info.version})을 받았습니다.`, detail: "다시 시작하면 적용됩니다. '나중에'를 누르면 다음에 켤 때 적용됩니다." })
          .then(r => { if (r.response === 0) { quitting = true; autoUpdater.quitAndInstall(); } });
      });
      autoUpdater.checkForUpdates().catch(() => {});
      setInterval(() => autoUpdater.checkForUpdates().catch(() => {}), 6 * 3600000);
    } catch (e) {}
  }
});
app.on("before-quit", () => { quitting = true; });
app.on("window-all-closed", e => { /* 트레이에 남음 */ });
