# UI Specification

**Product:** WaveWatch  
**Platform:** Android  
**Framework:** Jetpack Compose

---

## Navigation Structure

```
Bottom Navigation:
├── Home (Dashboard)
├── Alerts
├── Apps
└── Settings
```

---

## Screens

### 1. Dashboard (Home)

```
┌─────────────────────────────┐
│  🌊 WaveWatch               │
│  Your device is secure ✓    │
├─────────────────────────────┤
│  ┌─────────┐ ┌─────────┐   │
│  │   78    │ │   12    │   │
│  │ Score   │ │  Apps   │   │
│  └─────────┘ └─────────┘   │
├─────────────────────────────┤
│  [    🔍 SCAN NOW     ]     │
├─────────────────────────────┤
│  ⚠️ 2 Alerts                │
│  TikTok: High background... │
│  Unknown BT device nearby   │
└─────────────────────────────┘
```

### 2. Scan Results

```
┌─────────────────────────────┐
│  ← Security Scan            │
├─────────────────────────────┤
│  Score: 78/100  🟡          │
├─────────────────────────────┤
│  ✅ Apps: 8 safe, 2 risky   │
│  ✅ Network: Secure (WPA2)  │
│  ⚠️ Bluetooth: 1 unknown    │
├─────────────────────────────┤
│  Recommendations:           │
│  • Review TikTok permissions│
│  • Block unknown BT device  │
└─────────────────────────────┘
```

### 3. Alerts

```
┌─────────────────────────────┐
│  🔔 Alerts                  │
├─────────────────────────────┤
│  ┌─────────────────────┐   │
│  │ ⚠️ High background  │   │
│  │ TikTok used 500MB   │   │
│  │ while you slept     │   │
│  │                     │   │
│  │ [Restrict] [Ignore] │   │
│  └─────────────────────┘   │
│                             │
│  ┌─────────────────────┐   │
│  │ 📶 Unknown device   │   │
│  │ BT_Speaker nearby   │   │
│  │                     │   │
│  │ [Block] [Trust]     │   │
│  └─────────────────────┘   │
└─────────────────────────────┘
```

### 4. Apps List

```
┌─────────────────────────────┐
│  📱 Apps                    │
├─────────────────────────────┤
│  🎵 TikTok         🔴 Risky │
│     Camera, Mic, Location   │
│     1.8 GB today            │
├─────────────────────────────┤
│  💬 WhatsApp       🟢 Safe  │
│     Contacts, Storage       │
│     340 MB today            │
├─────────────────────────────┤
│  📺 YouTube        🟢 Safe  │
│     Storage                 │
│     1.2 GB today            │
└─────────────────────────────┘
```

### 5. Settings

```
┌─────────────────────────────┐
│  ⚙️ Settings                │
├─────────────────────────────┤
│  🔔 Smart Alerts      [ON]  │
│  📶 BT Scanner        [ON]  │
│  🌙 Dark Mode         [ON]  │
│  🔋 Battery Saver     [OFF] │
├─────────────────────────────┤
│  📊 Data Budget  →          │
│  📚 Learn        →          │
│  ℹ️ About        →          │
└─────────────────────────────┘
```

---

## Design Tokens

### Colors

| Token      | Light   | Dark    |
| ---------- | ------- | ------- |
| Primary    | #6366F1 | #818CF8 |
| Background | #FFFFFF | #0A0A0F |
| Surface    | #F1F5F9 | #1A1A24 |
| Success    | #10B981 | #34D399 |
| Warning    | #F59E0B | #FBBF24 |
| Danger     | #EF4444 | #F87171 |
| Text       | #0F172A | #F8FAFC |
| Muted      | #64748B | #94A3B8 |

### Typography

| Style   | Size | Weight   |
| ------- | ---- | -------- |
| Title   | 24sp | Bold     |
| Heading | 18sp | SemiBold |
| Body    | 14sp | Regular  |
| Caption | 12sp | Regular  |
| Small   | 10sp | Regular  |

### Spacing

| Token | Value |
| ----- | ----- |
| xs    | 4dp   |
| sm    | 8dp   |
| md    | 16dp  |
| lg    | 24dp  |
| xl    | 32dp  |

### Corner Radius

| Token | Value |
| ----- | ----- |
| sm    | 8dp   |
| md    | 12dp  |
| lg    | 16dp  |
| full  | 999dp |

---

## Components

- `SecurityScoreCard` — Circular score indicator
- `AlertCard` — Alert with actions
- `AppListItem` — App with permission badges
- `DeviceItem` — Bluetooth device row
- `ToggleSetting` — Settings toggle
- `ScanButton` — Primary CTA button
