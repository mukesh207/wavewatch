### **System Requirements Document (SRD)**

#### **1. Introduction**

WaveWatch is a mobile application designed to monitor and analyze mobile network traffic in real-time. The system leverages Kotlin for the frontend and C++ for the backend to provide real-time traffic monitoring, packet metadata analysis, usage control, smart alerts, and security insights for mobile users.

The system will also offer the ability to detect suspicious activity and provide information before and after taking action, such as terminating a suspicious app or process.

---

#### **2. System Architecture**

WaveWatch consists of the following major components:

* **Frontend (Mobile App)** :
* Built using Kotlin for Android and Kotlin Multiplatform for iOS.
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

**3.4 Usage Restriction Tools**

* Users can set data limits for mobile networks and Wi-Fi, and restrict background data usage for specific apps once the limit is reached.

**3.5 Security Insights**

* The backend will perform real-time analysis of network traffic to identify security risks such as unauthorized access or data exfiltration attempts.
* The app will alert the user if any suspicious network behavior is detected.

---

#### **4. System Requirements**

**4.1 Hardware Requirements**

* **Mobile Devices** : Android or iOS smartphones with the following minimum specifications:
* Android: Version 9.0 (Pie) or higher, 2GB RAM, 32GB storage.
* iOS: Version 12.0 or higher, 2GB RAM, 32GB storage.
* **Backend Infrastructure** :
* Cloud-based servers for data storage and processing.
* Secure API endpoints to communicate between mobile apps and backend.

**4.2 Software Requirements**

* **Mobile App** :
* Kotlin for Android development.
* Kotlin Multiplatform for iOS support.
* **Backend** :
* C++ for network traffic analysis and packet metadata processing.
* Database support (e.g., SQL or NoSQL) for storing user settings, data usage, and alerts.
* **Cloud Infrastructure** :
* Hosting platform (e.g., AWS, Azure) for scalable backend services.
* Secure communication protocols (e.g., HTTPS) to ensure data privacy and integrity.

---

#### **5. Security Considerations**

* **Data Privacy** :
* Sensitive data (e.g., user traffic patterns, packet metadata) will be anonymized and securely stored.
* No root access required for app functionality to avoid security risks on user devices.
* **Real-Time Monitoring** :
* Secure API communication between the mobile app and the backend.
* All suspicious activity alerts will be processed and stored securely.

---

#### **6. Performance Considerations**

* The system will be optimized to minimize impact on device performance.
  * Network traffic analysis will be lightweight to ensure it doesn’t drain the device’s battery or CPU.
  * Alerts and notifications will be processed in real-time with minimal latency.

---

#### **7. Scalability and Maintenance**

* The system should be able to handle an increasing number of users and network requests.
* Cloud-based backend services will be scalable to accommodate growing traffic.
* Regular updates will be pushed to both the mobile app and backend to fix bugs, improve performance, and add new features.

---

#### **8. User Requirements**

* **User Access** :
* Users will have the ability to interact with the app via a clean, intuitive interface.
* Notifications about suspicious activities will be actionable, with clear steps to either monitor or terminate the activity.
* **Knowledge of Suspicious Activity** :
* Users will receive detailed information about suspicious activities before taking action, and feedback afterward to ensure they understand the rationale for the decision made.

---
