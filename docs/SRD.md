
### **System Requirements Document (SRD)** 

#### **1. Introduction**

WaveWatch is a mobile application designed to monitor and analyze mobile network traffic in real-time. The system leverages Kotlin for the frontend and C++ for the backend to provide real-time traffic monitoring, packet metadata analysis, usage control, smart alerts, and security insights for mobile users. The system also incorporates features to detect suspicious activity, offer context before taking action, and provide comprehensive feedback post-action.

The goal of WaveWatch is to empower users with tools to manage their mobile network traffic securely and efficiently, preventing unauthorized or malicious activities on their devices.

---

#### **2. System Architecture**

WaveWatch consists of the following major components:

* **Frontend (Mobile App)** :
  * Built using Kotlin for Android and Kotlin Multiplatform (KMP) for iOS.
  * Provides real-time data visualization, alerts, and user interaction.
  * Displays information about suspicious activities and offers options to terminate them.
* **Backend (C++)** :
  * Responsible for processing network traffic data, analyzing metadata, and generating alerts.
  * Handles communication with mobile devices to fetch network data and send real-time notifications.
* **Cloud Infrastructure** :
  * Hosts the backend services and stores data securely.
  * Ensures scalability and supports real-time traffic analysis for all users.

---

#### **3. Functional Components**

**3.1 Network Traffic Monitoring**

* The backend will monitor mobile network data usage (e.g., 4G, 5G, Wi-Fi) in real-time and send usage statistics to the mobile app.
* The app will display data consumption by application and network type, enabling users to track their mobile usage.

**3.2 Packet Metadata Analysis**

* The system will analyze network packets to detect unusual or suspicious patterns, such as unauthorized data transfers or excessive consumption by specific apps.
* Detailed metadata of each network packet (e.g., source, destination, type, size) will be analyzed for security and performance optimization.

**3.3 Alerts and Notifications**

* **Suspicious Activity Detection** :
  * The backend will detect and flag suspicious activity based on predefined conditions, such as unauthorized data requests or excessive data consumption by specific apps.
* **Pre-Kill Notification** :
  * When suspicious activity is detected, the system will notify the user through the app, providing details such as:
    * **Type of Activity** : e.g., unusual data usage, malicious traffic, etc.
    * **Suspicious App/Process** : The app or process responsible for the activity.
    * **Reason for Alert** : Why the activity is flagged as suspicious.
    * **Potential Risks** : What could happen if the suspicious activity is not addressed.
* **Post-Kill Feedback** :
  * After the user takes action to kill or terminate the suspicious activity, the app will display:
    * **Confirmation** : “Suspicious app X has been terminated to prevent unauthorized data usage.”
    * **Reasoning** : Why the app was terminated and what risk was mitigated.
    * **Impact** : Any potential impact on the device (e.g., app functionality may be affected).
    * **Future Recommendations** : Guidance on preventing future occurrences (e.g., “Consider reviewing app permissions”).

**3.4 Smart Alerts & Kill Switch Control**

The system shall provide real-time alerts based on network, Bluetooth, and system behaviors, allowing users to stay informed and take immediate control actions. Additionally, the Kill Switch will handle malware or any suspicious software by giving users the ability to terminate processes.

##### **Alert Conditions** :

* **Screen Sharing** : Detected sessions where the screen is being shared with unknown or unauthorized devices.
* **Mobile Hotspot (Tethering) Status** : Active hotspot with connected devices.
* **Suspicious Traffic** : Foreign IPs, unknown Bluetooth connections.
* **Abnormal Background Usage** : BLE scans by inactive apps.
* **Malware Detection** : Identifying suspicious or malware activities based on app/system behavior.

##### **Alert Features** :

* Contextual alerts with real-time information (app name, traffic type, connected devices, suspicious app name).
* **Kill Switch Controls** for direct action:
  * Instantly  **terminate screen sharing** .
  * **Disable mobile hotspot** .
  * **Disconnect unknown devices** from hotspot.
  * **Terminate suspicious software** or malware processes.

##### **Kill Switch Workflow** :

1. **Alert Triggered** : Based on abnormal activities (e.g., screen sharing, hotspot usage, suspicious app behavior).
2. **User Control** : Kill Switch offers the option for immediate action to stop the activity.
3. **Action Taken** : User disables hotspot, stops sharing, disconnects devices, or kills a suspicious process.
4. **Feedback Provided** : Post-action feedback on the impact, reasoning, and preventive recommendations for the future.

**3.5 Usage Restriction Tools**

* Users can set data limits for mobile networks and Wi-Fi, and restrict background data usage for specific apps once the limit is reached.
* App provides a visual summary of data consumption and allows users to control usage in real-time.

**3.6 Security Insights**

* The backend will perform real-time analysis of network traffic to identify security risks such as unauthorized access or data exfiltration attempts.
* The app will alert the user if any suspicious network behavior is detected, offering mitigation steps.

---

#### **4. System Requirements**

**4.1 Hardware Requirements**

* **Mobile Devices** :
  * Android: Version 9.0 (Pie) or higher, 2GB RAM, 32GB storage.
  * iOS: Version 12.0 or higher, 2GB RAM, 32GB storage.
* **Backend Infrastructure** :
  * Cloud-based servers for data storage and processing (e.g., AWS, Azure).
  * Secure API endpoints to communicate between mobile apps and backend.

**4.2 Software Requirements**

* **Mobile App** :
  * Kotlin for Android development.
  * Kotlin Multiplatform for iOS support.
  * Jetpack Compose for UI development on Android.
  * SwiftUI (if necessary) for iOS app development.
* **Backend** :
  * C++ for network traffic analysis and packet metadata processing.
  * Database support (e.g., SQLite, NoSQL) for storing user settings, data usage, and alerts.
* **Cloud Infrastructure** :
  * Hosting platform (e.g., AWS, Azure) for scalable backend services.
  * Secure communication protocols (e.g., HTTPS) to ensure data privacy and integrity.

---

#### **5. Security Considerations**

* **Data Privacy** :
  * Sensitive data (e.g., user traffic patterns, packet metadata) will be anonymized and securely stored.
  * No root access required for app functionality to avoid security risks on user devices.
  * The app will follow best practices in securing personal and sensitive data.
* **Real-Time Monitoring** :
  * Secure API communication between the mobile app and the backend.
  * All suspicious activity alerts will be processed and stored securely, ensuring confidentiality and integrity of the data.
* **User Consent** :
  * The app will ask for explicit user consent before accessing sensitive data (e.g., network usage, app permissions).
  * User control over data collection and processing will be emphasized, allowing opt-in or opt-out features.

---

#### **6. Performance Considerations**

* The system will be optimized to minimize impact on device performance.
  * Network traffic analysis will be lightweight to ensure it doesn’t drain the device’s battery or CPU.
  * Alerts and notifications will be processed in real-time with minimal latency, ensuring timely action on suspicious activities.
* The backend will be optimized for low-latency communication and high throughput to handle multiple simultaneous requests without degradation in performance.

---

#### **7. Scalability and Maintenance**

* The system should be able to handle an increasing number of users and network requests.
* Cloud-based backend services will be scalable to accommodate growing traffic.
* Regular updates will be pushed to both the mobile app and backend to fix bugs, improve performance, and add new features.
* Automated testing and monitoring will be implemented to ensure continuous availability and system health.

---

#### **8. User Requirements**

* **User Access** :
  * Users will have the ability to interact with the app via a clean, intuitive interface.
  * Notifications about suspicious activities will be actionable, with clear steps to either monitor or terminate the activity.
  * Users will receive guidance before and after taking action, ensuring they understand the rationale for the decision made.
* **Knowledge of Suspicious Activity** :
  * Users will receive detailed information about suspicious activities, the risks associated, and the steps taken after resolving the issue.
  * The app will help users stay informed and proactive in managing their device’s security.

---

This SRD reflects the comprehensive system design, incorporating new features like smart alerts, kill switch controls, and a stronger focus on user experience and security.
