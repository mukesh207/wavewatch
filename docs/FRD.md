
# 📘 **WaveWatch – Feature Requirements Document (FRD)**

---

## 🧠 **Vision**

WaveWatch is a **privacy-first, no-root mobile network monitoring app** that helps users:

* Track real-time network usage
* Identify security risks
* Control background activity
* Understand device behavior across network types
* **Terminate suspicious or malware activities using a Kill Switch** for enhanced security.

---

## 🎯 **Core Features**

### 1. 📡 **Real-Time Network Monitoring**

* Track upload/download speed per app
* Monitor active network interface (Wi-Fi, Mobile, VPN)
* Visualize protocol usage (TCP, UDP, TLS, QUIC)

### 2. 📦 **Packet Metadata Analysis**

* Detect source/destination IPs & ports
* Identify DNS queries and their resolution time
* Monitor TLS handshakes and connection stability
* Estimate latency and packet loss passively

### 3. 🚨 **Smart Alerts**

WaveWatch shall provide real-time, context-aware alerts based on signal analysis. These alerts will help users identify risks and take action without needing technical expertise.

#### ✅ **Alert Conditions:**

* Sudden traffic spikes
* Suspicious foreign IPs
* Abnormal background usage
* Unknown Bluetooth device connections
* Silent BLE scans by inactive apps
* **Active screen sharing sessions**
* **Hotspot (tethering) usage with connected device count**

#### ✅ **Alert Capabilities:**

* In-app notifications with relevant metadata (app, IP, device, volume)
* Guidance via the chat-based assistant
* **Kill Switch Controls:**
  * Instantly **terminate screen sharing**
  * **Disable mobile hotspot**
  * **Disconnect unknown tethered devices**
  * **Terminate suspicious software or malware** based on behavior analysis
* **Post-action feedback** including:
  * Why the action was triggered
  * What was stopped (e.g., suspicious app or activity)
  * Risk explanation and future prevention tips

### 4. 🔐 **Security & Privacy Insights**

* Flag unencrypted HTTP/DNS traffic
* Detect tracker domains and analytics SDKs
* Monitor BLE usage for tracking behavior
* Display score for each app based on privacy leaks
* **Identify and flag suspicious apps or processes that may be malware.**

### 5. 📊 **Usage Dashboard**

* Visualize data consumption hourly, daily, weekly
* App-wise usage breakdown
* Bluetooth usage summary: duration, devices, scan frequency
* Forecasting based on historical trends

### 6. 💬 **Chat-based Assistant**

* Natural language queries like:
  * “Which app used the most data last night?”
  * “Any unknown Bluetooth devices nearby?”
  * “Show connections made over insecure protocols.”
  * “Which app is consuming the most battery and data?”
  * “What suspicious apps are running in the background?”

### 7. 🚫 **Usage Restriction Tools**

* Automatically block:
  * Background data for suspicious apps
  * Bluetooth at night or on low battery
* Optional App Lock during peak usage
* **Terminating suspicious software** via the **Kill Switch** based on user settings

### 8. 🛠️ **Developer Tools (Optional in Future)**

* App inspection mode for devs
* Visual packet pattern replay for testing connections

---

## 🧩 **Signal Categories**

### A. 📶 **Mobile/Wi-Fi Network Signals**

| Type            | Examples                                  |
| --------------- | ----------------------------------------- |
| Traffic Stats   | Data usage, protocol, per-app metrics     |
| Connection Info | RTT, latency, port scans, IP profiles     |
| DNS/TLS Events  | DNS resolution time, TLS handshake errors |

### B. 🔵 **Bluetooth Signals** (NEW)

| Type               | Examples                                              |
| ------------------ | ----------------------------------------------------- |
| Device Events      | Connected/disconnected, MAC, name, signal strength    |
| Scanning Behavior  | BLE scan frequency, unknown nearby devices            |
| App-Level Activity | App initiating scan or connection                     |
| Usage Metrics      | Total BT usage time, data exchanged, background scans |

### C. 📲 **System & User Behavior Signals**

* Foreground app mapping to network activity
* Battery drain vs. data usage
* Device state: screen off, charging, etc.

---

## ⚙️ **Architecture Highlights**

### 🔧 **C++ Backend**

* High-performance packet analysis engine
* DNS/TLS parsing and protocol inspection
* Memory-safe, multi-threaded signal pipeline

### 📱 **Kotlin KMP Frontend**

* Shared logic across Android/iOS
* Modern Compose UI for intuitive dashboards
* Flow-based signal stream using coroutines

### 📦 **Signal Collector Modules (Examples)**

* `NetworkSignalManager`
* `BluetoothSignalManager`
* `AppUsageSignalManager`
* `DNSResolverSignalEngine`

---

## 📂 **Bluetooth Signal Collector Design**

### Components

| File/Class                     | Role                                                                   |
| ------------------------------ | ---------------------------------------------------------------------- |
| `BluetoothSignalManager`     | Core event handler and scanner                                         |
| `BluetoothAlertEngine`       | Analyzes for background scans, device anomalies                        |
| `BluetoothDeviceProfile`     | Stores known device behavior patterns                                  |
| `BluetoothUsageStats`        | Logs duration, scan count, app interaction                             |
| `BluetoothRestrictionEngine` | Implements FRD’s restriction rules                                    |
| `BluetoothMalwareScanEngine` | Identifies suspicious Bluetooth activity for kill switch functionality |

---

This updated FRD now includes the **Kill Switch** feature to allow users to terminate suspicious software and malware activities, along with providing detailed alerts, feedback, and control over malicious behavior.
