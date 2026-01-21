# Feature Requirements Document (FRD)

**Product:** WaveWatch  
**Version:** 2.0  
**Platform:** Android

---

## Tagline

> **"Analyze. Protect. Learn."**
>
> Security analysis and threat management for Android — made simple.

---

## Feature Overview

```
┌─────────────────────────────────────────────────┐
│                  WaveWatch                      │
├─────────────────────────────────────────────────┤
│  ANALYZE        │  PROTECT       │  LEARN       │
│  ───────        │  ───────       │  ─────       │
│  Security Scan  │  Kill Switch   │  Tips        │
│  App Audit      │  Block Apps    │  Explanations│
│  Network Check  │  BT Blacklist  │  Reports     │
│  BT Scanner     │  Alerts        │  Articles    │
└─────────────────────────────────────────────────┘
```

---

## 1. Security Analysis (ANALYZE)

### 1.1 Security Score

- Overall score from 0-100
- Breakdown by category (Apps, Network, Bluetooth)
- Color coded: 🟢 Green (80+) | 🟡 Yellow (50-79) | 🔴 Red (<50)

### 1.2 App Permission Audit

| Check                 | Description                            |
| --------------------- | -------------------------------------- |
| Dangerous permissions | Camera, Microphone, Location, Contacts |
| Background access     | Apps running when phone is idle        |
| Data usage            | Which apps consume most data           |
| Battery drain         | Apps draining battery in background    |

### 1.3 Network Security Check

| Check                | Description                            |
| -------------------- | -------------------------------------- |
| WiFi encryption      | Is current WiFi using WPA2/WPA3?       |
| Open network warning | Alert when connecting to public WiFi   |
| Active connections   | Which apps are currently using network |

### 1.4 Bluetooth Scanner

| Check              | Description                         |
| ------------------ | ----------------------------------- |
| Nearby devices     | List all Bluetooth devices in range |
| Unknown detection  | Flag devices not in trusted list    |
| Connection history | Log of all BT connections           |

---

## 2. Threat Management (PROTECT)

### 2.1 Kill Switch

- One-tap emergency button
- Disconnects all suspicious connections
- Disables background data for flagged apps
- Visual confirmation of action taken

### 2.2 App Restrictions

| Action                   | How                                    |
| ------------------------ | -------------------------------------- |
| Block background data    | Links to Android settings              |
| Restrict permissions     | Links to app permission settings       |
| Uninstall recommendation | "This app is risky, consider removing" |

### 2.3 Bluetooth Blacklist

- Add devices to "blocked" list
- Auto-reject pairing requests from blocked devices
- Trusted devices list

### 2.4 Smart Alerts

| Alert Type            | Example                                   |
| --------------------- | ----------------------------------------- |
| High background usage | "TikTok used 500MB while you slept"       |
| Unknown BT device     | "New device nearby: BT_Speaker"           |
| Insecure network      | "You're on an open WiFi network"          |
| Risky app             | "App X has access to camera + microphone" |

---

## 3. Security Education (LEARN)

### 3.1 Plain English Explanations

Every alert includes:

- **What happened** (1 sentence)
- **Why it matters** (1 sentence)
- **What you can do** (action button)

Example:

```
⚠️ TikTok used 500MB in the background last night

WHY: Apps shouldn't use this much data when you're not using them.
     This could drain your data plan and battery.

ACTION: [Restrict Background Data]
```

### 3.2 Weekly Security Report

Every Sunday, show:

- Security score trend (up/down)
- Top 3 data-consuming apps
- Any new threats detected
- Tips for the week

### 3.3 Learn Section

Short articles (2-minute reads):

- "What are app permissions?"
- "How to stay safe on public WiFi"
- "Why Bluetooth can be dangerous"
- "Signs your phone might be compromised"

---

## 4. UI Screens

| Screen       | Purpose                            |
| ------------ | ---------------------------------- |
| Dashboard    | Security score, quick scan, alerts |
| Scan Results | Detailed analysis results          |
| Alerts       | List of notifications with actions |
| Apps         | Per-app security status            |
| Bluetooth    | Device list and blacklist          |
| Settings     | Preferences and toggles            |
| Learn        | Educational content                |

---

## 5. Android APIs Used

| Feature       | API                                      |
| ------------- | ---------------------------------------- |
| Data usage    | `NetworkStatsManager`                    |
| App list      | `PackageManager`                         |
| Permissions   | `PackageManager.getPermissionsInfo()`    |
| Bluetooth     | `BluetoothAdapter`, `BluetoothLeScanner` |
| WiFi info     | `WifiManager`, `ConnectivityManager`     |
| Battery stats | `UsageStatsManager`                      |
| Notifications | `NotificationManager`                    |

---

## 6. Permissions Required

| Permission             | Why                       |
| ---------------------- | ------------------------- |
| `PACKAGE_USAGE_STATS`  | To see app usage data     |
| `ACCESS_NETWORK_STATE` | To check network security |
| `BLUETOOTH_SCAN`       | To detect nearby devices  |
| `POST_NOTIFICATIONS`   | To send alerts            |
