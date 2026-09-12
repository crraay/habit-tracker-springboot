# Habit Tracker — v1 domain contract

This file is the product/domain law for v1. Backend is the system of record. The Angular UI must not invent a different meaning of *today*, check-in window, streak, pause, archive, or delete.

If code and this file disagree, stop. Either the code is wrong, or this file must be changed in an explicit decision. Do not silently invent a new rule.

Names in this file are the vocabulary for product and new code. Existing code still uses older names; see [Glossary](#glossary). Do not introduce a third synonym.

## Promise

Each day it is clear what to do, and it is honest whether you did it.

## Out of v1

Not in this contract, do not add “while you’re here”:

- non-daily schedules (weekdays, intervals, RRULE)
- quantitative habits (count toward a target)
- explicit skip / “pause streak for cause”
- reminders, push, email
- check-ins in the future
- shared or social habits
- XP, achievements, leaderboards
- rewriting history when rules change

## Entities

| Entity | Role |
|---|---|
| **User** | Owner of data and of the clock. |
| **Habit** | Daily yes/no intention with a lifecycle. |
| **Check-in** | Fact: this habit was done on this **local calendar date**. |

Presentation (name, description, icon, color) is not domain law.

## Glossary

This table is name law. Prefer the **this file** column in speech, docs, and new code. Do not rename the whole API in passing; a URL/`web-api` rename is a separate breaking change.

| This file | Meaning | Code today | Target when touching this area |
|---|---|---|---|
| **Check-in** | Fact: habit H was done on local date D | Entity/table `HabitLog` / `habit_log` | Persistence may stay `HabitLog`. Services/docs: check-in. API rename (`/api/check-ins`) only as an explicit breaking change |
| **mark** / **unmark** | Create or remove that fact | `trackHabit` / `untrackHabit`, `/api/habit-track` | Same verbs in product; API may keep `habit-track` until the breaking change above |
| **localDate** | Calendar day that identifies the check-in | `HabitLog.date` | Keep `date` on the column if needed; in APIs prefer `localDate` when adding fields |
| **completedAt** | Audit instant of the mark; does not define the day | `HabitLog.createdAt` (audit) | Explicit `completedAt` if a dedicated field is added |
| **today** | Calendar date in `user.timezone` | Not on the user. Scheduler uses `app.scheduling.timezone` | `user.timezone` + derive today from it |
| **timezone** | IANA zone that owns the user’s clock | Missing | `user.timezone` |
| **startDate** | First day the habit can be scheduled | Missing. Statistics path `{startDate}` means range-from | `habit.startDate` only. Range bounds: `from` / `to` |
| **state** | Habit lifecycle | Missing | Field `state`: `active` \| `paused` \| `archived`. Java enum `ACTIVE` / `PAUSED` / `ARCHIVED` |
| **done** | Whether that local date has a check-in | `HabitTrackResponse.status` (`Boolean`) | `done: boolean`. Never call this `status` |
| **pending** | Today, scheduled, not done | Missing | Derived; not a stored row |
| **miss** | Past scheduled day, not done | Missing | Derived; not a stored row |
| **scheduled day** | Date the habit was expected | Missing | Derived from `startDate` + `state` over time |
| **current streak** / **live streak** | Consecutive scheduled done days still attached to today or yesterday | `HabitAggregate.currentStreak` (different rule) | Keep field name; match this file’s rule |
| **best streak** | Longest live-run ever | `HabitAggregate.bestStreak` | Keep name |
| **UserStatus** | Account auth state (`PENDING_VERIFICATION`, …) | `User.status` | Unchanged. Not habit `state` |

Do **not** use `status` for habit lifecycle or for “day done”. That word is already `UserStatus` and the boolean on `HabitTrackResponse`.

## User and “today”

- Every user **must** have an IANA timezone (`Asia/Bangkok`, `Europe/Moscow`, …).
- **Today** is the calendar date in `user.timezone`, not server local time and not UTC.
- Check-in identity is that local date (`2026-09-12`), not a UTC instant.
- A timestamp (`completedAt`) may be stored for audit. It does not define the day.
- Changing timezone does **not** rewrite existing local dates. After the change, *today* uses the new zone.

## Habit

- A habit belongs to exactly one user. Every read/write is scoped to the owner.
- v1 habits are **binary** and **daily**: every **scheduled** day is expected.
- `startDate` is the first day the habit can be scheduled. Set at creation; not editable in v1.
- Days before `startDate` are outside the habit’s life. They are not misses.

### Lifecycle

Habit **state** (not `status`):

```
active  --pause-->   paused
paused  --resume-->  active
active | paused --archive--> archived
archived --restore--> paused
any existing --delete--> gone
```

| State | In “today” | Scheduled? | History |
|---|---|---|---|
| `active` | yes | yes | readable |
| `paused` | no | no | readable |
| `archived` | no | no | readable, not in working lists |
| deleted | — | — | gone |

- **Pause:** hidden from today; no misses accrue; live streak ends (see Streak).
- **Archive:** hidden from working lists; same scheduling rule as pause. Restore returns to **paused** (not active), so a gap of days cannot silently become misses. The user resumes explicitly.
- **Hard delete:** permanent. Deletes the habit and **all** of its check-ins. No undo in v1.

## Check-in

- Identity: `(habitId, localDate)`. At most one fact per pair.
- Marking **done** is **idempotent**: repeating the same day does not create a second row.
- Unmarking removes the fact (or equivalent). The day is again not done (`done = false`).
- **Allowed dates:** today and yesterday in `user.timezone`. Not further back. Not the future.
- Unmark is allowed for the same window (today and yesterday).
- Only an **active** habit can be marked or unmarked. Paused/archived habits reject check-in writes.
- There is no `missed` row. A miss is derived: the day was scheduled, the date is before today, and no check-in exists.
- Today without a check-in is **pending**, not missed.

## Scheduled day

A date `D` is scheduled for a habit when all of:

1. `D >= startDate`
2. the habit’s **state** on `D` was `active`
3. `D` is not in the future relative to the user’s today

Pause and archive are holes in the schedule, not failures.

History is facts. State is policy. Changing state does not rewrite past check-ins.

## Streak

- **Live (current) streak:** length of the consecutive run of scheduled days with a check-in that is still attached to today or yesterday.
  - If today is done, the run may include today.
  - If today is not done, the run is evaluated from yesterday backward.
  - A scheduled day without a check-in ends the live run.
- Pause and archive **end the live run**. They do not count as misses. After resume, current streak starts over (`0` until the next qualifying run).
- **Best streak:** maximum live-run length ever observed for that habit, including runs that already ended. Delete of the habit deletes this too.

Do not apply a new scheduling policy to old days. v1 has only daily-while-active, so this matters the day a future version adds weekdays.

## Statistics (v1)

Compute only from scheduled days inside the habit’s life:

- current streak
- best streak
- completion rate over the last **7** and **30** scheduled days (not last 7 calendar days if some were paused)
- month calendar cells: done / not done (scheduled, not pending today) / outside life / pending today

## Authority

- Backend enforces every rule above (window, state, uniqueness, ownership, timezone).
- The UI may hide illegal actions, but hidden UI is not security and not the source of truth.
- Tests that lock these invariants are the executable copy of this file. If a test and this file disagree, that is a bug.

## Change control

Edits to this file are product decisions, not cleanup. If a change is needed, update this file first, then code and tests, then the pointers in `AGENTS.md` if the meaning of the pointer changed.
