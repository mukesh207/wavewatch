# dWaveWatch: SDLC Document

## Project Overview

**WaveWatch** is a mobile application powered by a C++ backend and Kotlin/KMP frontend designed to monitor, analyze, and control mobile network traffic. The app identifies suspicious activities, alerts the user, and allows them to take control actions such as killing or quarantining the threat. It aims to provide security and awareness for all types of mobile users.

---

## SDLC Phases

### 1. Requirement Analysis

#### Functional Requirements

* Monitor network traffic (WiFi, Bluetooth, Mobile Data).
* Detect and classify suspicious activities in real time.
* Notify users when an anomaly or threat is found.
* Allow users to terminate/disable malicious activities.
* Provide users with:
  * Pre-kill information about the threat.
  * Post-kill summary and recommendations.
* Display history logs and analytics of past incidents.
* Seamless user experience with multi-platform support via Kotlin KMP.
* Backend built in C++ for efficiency and performance.

#### Non-Functional Requirements

* Platform compatibility: Android, iOS.
* High performance for real-time traffic analysis.
* Offline functionality for certain features.
* Secure data handling and transmission.
* Accessible UI for all user types.

---

### 2. System Design

#### High-Level Architecture

* **Frontend** : Kotlin KMP-based app with platform-specific UI support.
* **Backend** : C++ modules analyzing and processing low-level traffic data.
* **Bridge Layer** : C++ <-> Kotlin communication using JNI or KMP-native interoperability.
* **Notification Layer** : Manages alerts, warnings, and user interaction.
* **Database** : Local (SQLite or Room DB) for logs/history.
* **Cloud Sync (Optional)** : For user backup and report sharing.

#### New Features Incorporated

* User-controlled "Kill Switch" for detected threats.
* Educational UI elements showing "What is this activity?", "Why it’s dangerous", "What we did".
* Modular backend to plug in more analysis models (e.g., ML in future).
* UI toggle to enable/disable specific network scanners.

---

### 3. Implementation

#### Development Tools

* **Frontend** : Kotlin, Jetpack Compose, Swift (if needed)
* **Backend** : C++ (optimized with CMake)
* **Build System** : Gradle, CMake
* **CI/CD** : GitHub Actions
* **VCS** : Git, GitHub
* **Testing** : JUnit, KotlinTest, gtest (for C++)

#### Modules

* `network-monitor`: Packet capture and classification.
* `suspicion-engine`: Analyze and match patterns.
* `notification-core`: Generates alert and user prompts.
* `control-center`: Allows user to take action (Kill/Ignore).
* `info-dash`: Summary, reports, education, and logs.

---

### 4. Testing

#### Testing Strategy

* **Unit Testing** : Core functions in Kotlin and C++
* **Integration Testing** : Kotlin ↔ C++ modules
* **UI Testing** : Espresso, Compose Test, XCUITest (iOS)
* **Security Testing** : Injection handling, permissions abuse
* **Performance Testing** : Load testing of background scanners

#### Example Test Cases

* Suspicious device triggers a notification.
* User presses “Kill” and the activity is terminated.
* Information is correctly shown before/after action.
* App doesn't crash during concurrent scans.

---

### 5. Deployment

#### Target Platforms

* Android 10+
* iOS 14+

#### Release Plan

* Internal Alpha → Closed Beta → Public Beta → Release v1.0
* Progressive rollout via Play Store and TestFlight.

#### DevOps

* CI/CD setup using GitHub Actions
* Version tagging: `v1.0.0`, `v1.1.0-beta`
* GitHub Releases for major updates

---

### 6. Maintenance

* Monitor crash reports and logs.
* Collect feedback from users for improvements.
* Weekly updates during early stages.
* Monthly patches after release stabilization.
* Add-on modules and threat signature updates via OTA.

---

### 7. Documentation

* SRS (Software Requirement Spec)
* SDD (System Design Doc)
* SDLC (This file)
* README, Wiki
* Security & Privacy Policy
* API reference (if backend modules exposed)

---

### 8. Project Timeline (March 2025 – February 2026)

| Phase                                                | Timeframe     |
| ---------------------------------------------------- | ------------- |
| Requirement Analysis                                 | 1 week        |
| Design (UI + System)                                 | 2 weeks       |
| Initial Implementation                               | 4 weeks       |
| Feature Modules (Kill switch, Suspicious Info, etc.) | 6 weeks       |
| Internal Testing                                     | 2 weeks       |
| Beta Release                                         | 2 weeks       |
| Feedback Loop & Bug Fixes                            | 3 weeks       |
| Final Release (before Science Day 2026)              | February 2026 |

---

### Contributors

* Solo Developer: Mukesh 👨‍💻 and friendly AI
* 

---
