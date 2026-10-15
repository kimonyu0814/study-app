// 알림 내용 계산 (안드로이드 앱의 Study.kt · TaePlan.kt 와 같은 규칙)
"use strict";

const WD = ["월", "화", "수", "목", "금", "토", "일"];
const pad2 = n => String(n).padStart(2, "0");

/* 한국 시간 기준 날짜 · 시각 */
function kst(d = new Date()) {
  const k = new Date(d.getTime() + 9 * 3600000);
  const date = `${k.getUTCFullYear()}-${pad2(k.getUTCMonth() + 1)}-${pad2(k.getUTCDate())}`;
  const hm = `${pad2(k.getUTCHours())}:${pad2(k.getUTCMinutes())}`;
  const wd = (k.getUTCDay() + 6) % 7;            // 0=월 … 6=일
  return { date, hm, wd };
}
function addDays(date, n) {
  const t = new Date(date + "T00:00:00Z"); t.setUTCDate(t.getUTCDate() + n);
  return t.toISOString().slice(0, 10);
}
function label(date) {
  const t = new Date(date + "T00:00:00Z");
  return `${t.getUTCMonth() + 1}/${t.getUTCDate()}(${WD[(t.getUTCDay() + 6) % 7]})`;
}
function kid(hm) {
  const m = /^(\d{1,2}):(\d{2})$/.exec(hm || ""); if (!m) return hm || "";
  const h = +m[1]; return `${h < 12 ? "오전" : "오후"} ${h % 12 || 12}:${m[2]}`;
}
function kidRange(s, e) {
  const a = kid(s); if (!e) return a;
  const b = kid(e); return a.slice(0, 2) === b.slice(0, 2) ? `${a}–${b.slice(3)}` : `${a}–${b}`;
}

/* ----- 온유: 학습기록 할 일 요약 ----- */
function summarize(data) {
  const { date: t } = kst(), tm = addDays(t, 1);
  const items = [];
  let dday = null;
  if (data) {
    const term = ((data.terms || {})[data.curTerm || "1-2"]) || {};
    const subs = term.subjects || {};
    (term.subjList || []).forEach(sj => {
      ["mid", "fin"].forEach(ex => {
        const o = (subs[sj.k] || {})[ex] || {};
        (o.todo || []).forEach(it => { if (it.date && it.t) items.push({ d: it.date, t: it.t, done: !!it.done, n: sj.n }); });
        (o.hw || []).forEach(it => { if (it.due && it.t) items.push({ d: it.due, t: "[학원 숙제] " + it.t, done: !!it.done, n: sj.n }); });
      });
    });
    (data.cal || []).forEach(it => { if (it.date && it.t) items.push({ d: it.date, t: it.t, done: !!it.done, n: "공통" }); });
    const ed = term.examDates || {};
    for (const [ex, n] of [["mid", "중간"], ["fin", "기말"]]) {
      if (dday || !ed[ex] || ed[ex] < t) continue;
      const diff = Math.round((new Date(ed[ex] + "T00:00:00Z") - new Date(t + "T00:00:00Z")) / 86400000);
      dday = `${n} ${diff === 0 ? "D-day" : "D-" + diff}`;
    }
  }
  items.sort((a, b) => a.d < b.d ? -1 : a.d > b.d ? 1 : 0);
  return {
    today: items.filter(x => x.d === t), tomorrow: items.filter(x => x.d === tm),
    late: items.filter(x => x.d < t && !x.done), dday,
    allDone: items.filter(x => x.done).length, all: items.length
  };
}
const line = i => (i.done ? "✓ " : "□ ") + i.n + " · " + i.t;
function studyNote(kind, s) {
  const lines = [], done = s.today.filter(x => x.done).length;
  let title;
  if (kind === "am") {
    title = `오늘 할 일 ${s.today.length}개` + (s.late.length ? ` · 밀린 일 ${s.late.length}개` : "") + (s.dday ? ` · ${s.dday}` : "");
    s.today.forEach(x => lines.push(line(x)));
    if (!s.today.length) lines.push("오늘 잡힌 할 일이 없습니다.");
  } else {
    const left = s.today.length - done;
    title = `오늘 ${done}/${s.today.length} 완료` + (left > 0 ? ` · 남은 일 ${left}개` : "");
    s.today.filter(x => !x.done).forEach(x => lines.push(line(x)));
    if (s.today.length && !left) lines.push("오늘 할 일을 모두 끝냈습니다!");
    if (s.tomorrow.length) lines.push("내일: " + s.tomorrow.map(x => x.t).join(", "));
    if (!lines.length) lines.push("오늘 잡힌 할 일이 없습니다.");
  }
  s.late.slice(0, 3).forEach(x => lines.push(`밀림 ${label(x.d)} ${x.n} · ${x.t}`));
  return { title, body: lines.join("\n") };
}

/* ----- 태은: 시간표 알림 ----- */
const TAE_DEFAULT = [
  [0, "15:00", "", "귀가 (방과후 줄넘기 후)"], [0, "15:30", "17:00", "미술"], [0, "17:30", "19:00", "영어"], [0, "19:00", "19:40", "🍚 저녁"], [0, "19:40", "21:00", "집공부 (국어, 수학)"],
  [1, "14:30", "", "귀가"], [1, "15:00", "15:50", "피아노"], [1, "16:00", "18:00", "집공부"], [1, "18:00", "18:40", "🍚 저녁"], [1, "18:40", "21:00", "집공부 (영숙이, 국어, 수학)"],
  [2, "13:50", "", "귀가"], [2, "14:30", "16:00", "영어"], [2, "16:00", "17:30", "빈 시간 (집공부 가능)"], [2, "17:30", "19:00", "한우리"], [2, "19:00", "19:40", "🍚 저녁"], [2, "19:40", "21:00", "집공부 (영숙이, 수학)"],
  [3, "13:50", "", "귀가"], [3, "15:00", "16:30", "영어"], [3, "17:00", "17:50", "피아노"], [3, "18:00", "18:40", "🍚 저녁"], [3, "18:40", "21:00", "집공부 (국어, 수학)"],
  [4, "13:50", "", "귀가"], [4, "15:10", "16:30", "생명과학"], [4, "16:30", "17:30", "집공부"], [4, "17:30", "18:10", "🍚 저녁"], [4, "18:30", "19:30", "축구"], [4, "19:40", "21:00", "집공부 (영숙이, 국어, 수학)"]
].map(([d, s, e, t]) => ({ d, s, e, t }));

function taeEvents(family, date) {
  const wd = (new Date(date + "T00:00:00Z").getUTCDay() + 6) % 7;
  const t = family && family.schedule && family.schedule.taeeun;
  if (!t) return TAE_DEFAULT.filter(x => x.d === wd);
  const mon = addDays(date, -wd), wk = (t.weeks || {})[mon] || {};
  const skip = new Set(wk.skip || []);
  return [...(t.fixed || []).filter(e => +e.d === wd && !skip.has(e.id)), ...(wk.add || []).filter(e => +e.d === wd)];
}
const minus = (hm, m) => { const [h, mi] = hm.split(":").map(Number); const t = h * 60 + mi - m; return `${pad2(Math.floor(t / 60))}:${pad2(t % 60)}`; };
/* 오늘 태은이 알림 목록: [{at:"HH:MM", title, body, pref}] */
function taeNotes(family, date) {
  const out = [], evs = taeEvents(family, date), wd = (new Date(date + "T00:00:00Z").getUTCDay() + 6) % 7;
  let study = false;
  evs.forEach(e => {
    if (!/^\d{2}:\d{2}$/.test(e.s || "")) return;
    const span = kidRange(e.s, e.e);
    if (/집공부/.test(e.t)) {
      study = true;
      const m = /\((.*)\)/.exec(e.t);
      out.push({ at: e.s, title: "집공부 시간이에요 📚", body: m ? `오늘 할 것: ${m[1]} (${span})` : `공부를 시작해요 (${span})`, pref: "n_study" });
    } else if (!/귀가|저녁|빈 시간/.test(e.t)) {
      out.push({ at: minus(e.s, 30), title: `30분 뒤 ${e.t}`, body: `${span} · 준비물 챙겨요!`, pref: "n_class" });
    }
  });
  if (study && wd <= 4) out.push({ at: "21:00", title: "오늘 공부 끝! 🎉", body: "수고했어요. 푹 쉬어요.", pref: "n_done" });
  return out;
}

module.exports = { kst, addDays, label, kid, kidRange, summarize, studyNote, taeEvents, taeNotes };
