# Development Roadmap

**Product:** WaveWatch  
**Platform:** Android  
**Timeline:** 10 weeks

---

## Phase 1: Setup & Design (Week 1-2)

### Week 1

- [ ] Set up Android project (Kotlin + Compose)
- [ ] Configure Gradle dependencies
- [ ] Create project structure (MVVM + Clean Architecture)
- [ ] Design system setup (colors, typography, components)

### Week 2

- [ ] Finalize UI/UX in Figma
- [ ] Create all Compose screens (empty states)
- [ ] Set up navigation (Jetpack Navigation)
- [ ] Bottom navigation bar

---

## Phase 2: Core Features (Week 3-6)

### Week 3: Security Scan

- [ ] Implement `PackageManager` integration
- [ ] Scan apps for dangerous permissions
- [ ] Calculate security score
- [ ] Display scan results

### Week 4: Network & Data

- [ ] Implement `NetworkStatsManager` integration
- [ ] Show data usage per app
- [ ] WiFi security check (`WifiManager`)
- [ ] Network status indicators

### Week 5: Bluetooth

- [ ] Implement Bluetooth scanning
- [ ] Device list with trusted/unknown status
- [ ] Blacklist functionality
- [ ] Alert for unknown devices

### Week 6: Alerts & Notifications

- [ ] Implement alert system
- [ ] Push notifications for threats
- [ ] Alert history (Room database)
- [ ] Action buttons (Block/Ignore)

---

## Phase 3: Polish (Week 7-8)

### Week 7

- [ ] Kill Switch implementation
- [ ] Settings screen with toggles
- [ ] Weekly report feature
- [ ] Learn section with articles

### Week 8

- [ ] Dark mode support
- [ ] Performance optimization
- [ ] Battery usage optimization
- [ ] UI polish and animations

---

## Phase 4: Testing & Launch (Week 9-10)

### Week 9

- [ ] Internal testing
- [ ] Bug fixes
- [ ] User testing with non-tech users
- [ ] Feedback iteration

### Week 10

- [ ] Final bug fixes
- [ ] Play Store assets (screenshots, description)
- [ ] Beta release (Play Console)
- [ ] Public launch

---

## Milestones

| Milestone | Date    | Deliverable                     |
| --------- | ------- | ------------------------------- |
| M1        | Week 2  | UI complete (screens navigable) |
| M2        | Week 6  | Core features working           |
| M3        | Week 8  | Feature complete                |
| M4        | Week 10 | Public launch                   |

---

## Success Criteria

- ✅ App works on Android 8.0+
- ✅ Security scan completes in <10 seconds
- ✅ Non-tech user can understand all alerts
- ✅ Battery drain <2% per day
- ✅ No crashes in beta testing
