# Software Requirements Specification (SRS)

**Product:** WaveWatch  
**Version:** 2.0  
**Platform:** Android 8.0+ (API 26+)

---

## 1. Introduction

### 1.1 Purpose

WaveWatch is a mobile security app that helps Android users:

- Understand their device's security status
- Protect against threats
- Learn about mobile security

### 1.2 Tagline

> **"Analyze. Protect. Learn."**

---

## 2. Functional Requirements

### 2.1 Dashboard (FR-001)

| ID       | Requirement                                |
| -------- | ------------------------------------------ |
| FR-001.1 | Display security score (0-100)             |
| FR-001.2 | Show "Scan Now" button                     |
| FR-001.3 | Display active alerts count                |
| FR-001.4 | Show quick stats (data usage, active apps) |

### 2.2 Security Scan (FR-002)

| ID       | Requirement                                   |
| -------- | --------------------------------------------- |
| FR-002.1 | Scan installed apps for dangerous permissions |
| FR-002.2 | Check WiFi network security                   |
| FR-002.3 | Scan for nearby Bluetooth devices             |
| FR-002.4 | Calculate overall security score              |
| FR-002.5 | Display results in simple language            |

### 2.3 Alerts (FR-003)

| ID       | Requirement                                |
| -------- | ------------------------------------------ |
| FR-003.1 | Send notification for security threats     |
| FR-003.2 | Show explanation for each alert            |
| FR-003.3 | Provide action button (Block/Allow/Ignore) |
| FR-003.4 | Store alert history                        |

### 2.4 App Management (FR-004)

| ID       | Requirement                              |
| -------- | ---------------------------------------- |
| FR-004.1 | List all apps with permission summary    |
| FR-004.2 | Show data usage per app                  |
| FR-004.3 | Link to Android settings to restrict app |
| FR-004.4 | Flag apps with risky permissions         |

### 2.5 Bluetooth Management (FR-005)

| ID       | Requirement                        |
| -------- | ---------------------------------- |
| FR-005.1 | Scan for nearby Bluetooth devices  |
| FR-005.2 | Show trusted vs unknown devices    |
| FR-005.3 | Allow adding devices to blacklist  |
| FR-005.4 | Alert when unknown device detected |

### 2.6 Kill Switch (FR-006)

| ID       | Requirement                                   |
| -------- | --------------------------------------------- |
| FR-006.1 | One-tap button to disable suspicious activity |
| FR-006.2 | Visual confirmation of action                 |
| FR-006.3 | Log what was blocked                          |

### 2.7 Learn Section (FR-007)

| ID       | Requirement               |
| -------- | ------------------------- |
| FR-007.1 | Display security tips     |
| FR-007.2 | Show educational articles |
| FR-007.3 | Weekly security report    |

---

## 3. Non-Functional Requirements

### 3.1 Performance

| ID      | Requirement                |
| ------- | -------------------------- |
| NFR-001 | App launch < 2 seconds     |
| NFR-002 | Security scan < 10 seconds |
| NFR-003 | Battery usage < 2% per day |

### 3.2 Usability

| ID      | Requirement                              |
| ------- | ---------------------------------------- |
| NFR-004 | All text readable by non-technical users |
| NFR-005 | No technical jargon in UI                |
| NFR-006 | Support dark mode                        |

### 3.3 Security

| ID      | Requirement                       |
| ------- | --------------------------------- |
| NFR-007 | No data sent to external servers  |
| NFR-008 | All data stored locally on device |
| NFR-009 | No root access required           |

### 3.4 Compatibility

| ID      | Requirement                            |
| ------- | -------------------------------------- |
| NFR-010 | Support Android 8.0 (API 26) and above |
| NFR-011 | Support both phones and tablets        |

---

## 4. System Architecture

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │
│         (Jetpack Compose UI)            │
├─────────────────────────────────────────┤
│            Domain Layer                 │
│    (Use Cases, Business Logic)          │
├─────────────────────────────────────────┤
│             Data Layer                  │
│  (Repositories, Local Database)         │
├─────────────────────────────────────────┤
│          Android System APIs            │
│  NetworkStats | UsageStats | Bluetooth  │
└─────────────────────────────────────────┘
```

---

## 5. Data Storage

| Data          | Storage           |
| ------------- | ----------------- |
| Settings      | SharedPreferences |
| Alert history | Room Database     |
| Scan results  | Room Database     |
| BT blacklist  | Room Database     |

---

## 6. Constraints

- No root access — must use standard Android APIs only
- `NetworkStatsManager` updates every ~2 hours (Android limitation)
- Cannot force-stop other apps without root
- Background scanning limited by Android battery optimization
