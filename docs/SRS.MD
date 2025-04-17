
### **Software Requirements Specification (SRS)**

#### **1. Introduction**

WaveWatch is a mobile application designed to monitor and analyze mobile network traffic in real-time, focusing on network performance, security, and data usage management. The app provides users with real-time alerts, metadata analysis, and smart actions to control suspicious activities on their mobile networks.

#### **2. Functional Requirements**

**2.1 User Interface (UI):**

* Clean and user-friendly design.
* Dashboard displaying real-time traffic data, alerts, and notifications.
* Chat-based assistant for troubleshooting and support.

**2.2 Network Traffic Monitoring:**

* Real-time tracking of mobile network data usage (e.g., 4G, 5G, Wi-Fi).
* Display of data consumption by app and network type.
* Breakdown of data usage for each application and network.

**2.3 Packet Metadata Analysis:**

* View detailed metadata for network packets (source, destination, type, size).
* Analyzing network traffic for unusual patterns or security threats.

**2.4 Alerts and Notifications:**

* Real-time alerts for exceeding data usage limits.
* Alerts for suspicious or potentially malicious network activity.
* Customizable alert settings based on user preferences.

---

### **🔔 Smart Alerts – System Features (SRS)**

**3.x Smart Alerts & Kill Switch Control**
The system shall provide real-time alerts based on a variety of monitored signals and detected anomalies. These alerts will enable users to monitor activity and take immediate control actions through an in-app Kill Switch mechanism.

**✅ Alert Conditions include:**

* Abnormal or high-volume data usage.
* Suspicious or unknown app-level traffic.
* Active screen sharing sessions consuming significant bandwidth.
* Mobile hotspot (tethering) activation, including connected devices.
* Bluetooth discovery events or unexpected scanning.
* Background usage of sensitive permissions or services.

**✅ Alert Capabilities:**

* Notify users with contextual information (e.g., app name, traffic stats, device count).
* Provide actionable suggestions via the chat-based assistant.
* Enable Kill Switch actions, allowing users to:
  * Instantly terminate screen sharing.
  * Disable mobile hotspot.
  * Disconnect unknown devices from hotspot.
* Offer post-action confirmation, impact explanation, and preventive guidance.

**2.5 Usage Restriction Tools:**

* Set limits on data usage for mobile networks and Wi-Fi.
* Restrict background data usage for specific apps when usage limits are reached.

**2.6 Security Insights:**

* Real-time analysis of network traffic to identify security risks.
* Alerts for unusual network behavior or suspicious apps consuming excessive data.

**2.7 Suspicious Activity Knowledge:**

*Before Killing Suspicious Activity:*

* The app will notify the user when suspicious activity is detected, providing detailed information such as:
  * **Type of Activity** : Unusual outbound traffic, unauthorized app behavior, excessive data consumption, etc.
  * **Suspicious App/Process** : The app or process causing the suspicious activity.
  * **Reason for Alert** : Explanation of why the activity is considered suspicious, based on traffic patterns or known security risks.
  * **Potential Risks** : Risks of leaving the suspicious activity unchecked, such as data leakage, malware, or unauthorized access.

**Example Alert:**

“Suspicious app detected: App X has been consuming excessive data. It may be trying to upload personal data without your permission.”

*After Killing Suspicious Activity:*

* After the user terminates the suspicious activity, the app will:
  * Provide a confirmation message:

    “Suspicious app X has been terminated to prevent unauthorized data usage.”
  * Display a post-action explanation:

    “App X was using unusual network traffic and could compromise your privacy.”
  * Provide future recommendations:

    “Consider reviewing app permissions or running a security scan to ensure your device is secure.”
  * Inform the user of the impact on device functionality (if applicable):

    “This action may impact the app’s ability to sync data until reconfigured.”

---

#### **3. Non-Functional Requirements**

* **Performance** : The app should run efficiently with minimal impact on device battery life and CPU usage.
* **Security** : The app must prioritize user privacy, ensuring that suspicious activities are handled securely.
* **Usability** : The information provided before and after taking action should be clear, concise, and user-friendly.
* **Scalability** : The system should handle a growing number of users without performance degradation.
* **Cross-Platform Compatibility** : The app should be compatible with both Android and iOS platforms.
* **Offline Support** : The app should provide limited functionality when no network connection is available.

---

#### **4. Security Requirements**

* The app will ensure secure communication between the mobile device and backend server for processing network data.
* Only authorized actions will be taken (e.g., killing an app or blocking network traffic) with user consent.
* All data related to suspicious activity detection will be handled securely, following privacy regulations.

---

#### **5. User Stories**

* **As an end user** , I want to monitor my data usage in real-time so that I can avoid exceeding my data limit.
* **As a mobile network user** , I want to receive alerts when suspicious network activity is detected so that I can protect my privacy.
* **As an app user** , I want to be able to restrict mobile data usage and set limits for specific apps to avoid high data consumption.
* **As a security-conscious individual** , I want to understand why an activity is suspicious before I take action so that I can make informed decisions.
* **As a user** , I want the app to provide feedback and recommendations after terminating suspicious activities so that I can ensure my device’s security.

---

#### **6. Assumptions**

* Users will have access to the app on modern smartphones running Android or iOS.
* The app will be updated regularly to maintain compatibility with new OS versions and introduce new features.
* The backend infrastructure will be secure and scalable, ensuring smooth operation even as the user base grows.

---

#### **7. Dependencies**

* **Mobile Network Data Access** : The app requires access to mobile network data usage statistics, which may vary depending on the device manufacturer and OS version.
* **Backend Infrastructure** : Cloud hosting and data processing services will be set up to support real-time traffic monitoring and analysis.
* **Security Standards** : The app will comply with data privacy laws and security regulations to protect user data.

---

#### **8. Risks**

* **Device Compatibility** : Different devices may present compatibility challenges due to variations in mobile network stack implementations.
* **Privacy Concerns** : Handling sensitive user data will require strong security measures to prevent breaches.
* **Performance** : Real-time data analysis may impact performance, particularly on low-end smartphones.

---

This **SRS** should be more structured, with clear bullet points and headings to make each section easier to navigate.
