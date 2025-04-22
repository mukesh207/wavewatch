## 📱 **UI Screens & Essential Modules**

### 1. **🔓 Onboarding & Permissions**

* Welcome screen
* Permissions (network, Bluetooth, usage access, battery optimization)
* Quick tutorial / intro

### 2. **🏠 Home / Dashboard**

* Real-time upload/download speed graph
* Active connections (App + IP + Interface)
* Summary widget: alerts, usage, battery

### 3. **📊 Usage Dashboard**

* Hourly, daily, weekly data usage graphs
* App-wise network + battery usage
* Bluetooth usage stats: scan frequency, devices, durations

### 4. **🚨 Smart Alerts Feed**

* List of triggered alerts
* Tap to view:
  * Metadata (app, IP, type)
  * “Take Action” button (Kill switch, restrict, allow)
  * “Why this alert?” explanation

### 5. **🛡️ Security & Privacy Insights**

* App-wise scores (privacy leaks)
* Unencrypted traffic tracker
* Analytics SDKs and trackers

### 6. **🚫 Kill Switch Center**

* Log of terminated activities
* Re-enable option (if safe)
* Post-action summary: reason, app, what was terminated

### 7. **💬 Chat-Based Assistant**

* Natural language input box
* Chat response stream
* Tap to auto-take action or open detailed view

### 8. **⚙️ Settings**

* General (theme, notification settings)
* Auto restriction rules
* Advanced: toggle developer mode, logs, feedback

---

## 🧱 **UI Flow Diagram (Simplified)**

```
[Onboarding]
     ↓
[Home Dashboard]
  ↙   ↓   ↘   ↘
[Alerts][Usage][Privacy][Chat Assistant]
         ↓        ↓            ↓
     [App Detail] [Kill Switch] [Query Result]
```

---

## 🧩 **Common Components**

These components should be shared across screens for UX consistency:

* **App card** : with name, icon, network/battery stats
* **Alert card** : triggered alert summary + quick action
* **Mini usage chart** : line or bar
* **Assistant bubble** : Chat icon persistent on major screens
* **Snackbar/Toast** : for quick success/failure action updates
