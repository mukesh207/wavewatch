# Software Development Lifecycle (SDLC)

**Product:** WaveWatch  
**Methodology:** Agile (Solo Developer)

---

## Overview

```
┌──────────┐   ┌──────────┐   ┌──────────┐   ┌──────────┐
│  PLAN    │ → │  BUILD   │ → │  TEST    │ → │  DEPLOY  │
│ 2 weeks  │   │ 6 weeks  │   │ 1 week   │   │ 1 week   │
└──────────┘   └──────────┘   └──────────┘   └──────────┘
```

---

## 1. Planning Phase (2 weeks)

### Deliverables

- [x] README.md
- [x] PRD.md
- [x] FRD.md
- [x] SRS.md
- [x] UI wireframe (prototype.html)
- [x] roadmap.md

### Activities

- Define scope and features
- Create UI mockups
- Validate technical feasibility
- Set up project repository

---

## 2. Build Phase (6 weeks)

### Week-by-Week Plan

| Week | Focus         | Deliverable               |
| ---- | ------------- | ------------------------- |
| 1    | Project setup | Empty app with navigation |
| 2    | UI screens    | All screens (static)      |
| 3    | Security scan | App audit feature         |
| 4    | Network/Data  | Usage tracking            |
| 5    | Bluetooth     | BT scanner                |
| 6    | Alerts        | Notification system       |

### Development Practices

- Daily commits
- Feature branches
- Code review (self-review checklist)
- Incremental builds

---

## 3. Test Phase (1 week)

### Testing Types

| Type           | Method                  |
| -------------- | ----------------------- |
| Unit tests     | JUnit + MockK           |
| UI tests       | Compose Testing         |
| Manual testing | Physical devices        |
| User testing   | 5 non-tech beta testers |

### Test Devices

- Samsung Galaxy (Android 12)
- Pixel (Android 14)
- Older device (Android 9)

---

## 4. Deploy Phase (1 week)

### Checklist

- [ ] Create Play Store listing
- [ ] Upload screenshots
- [ ] Write app description
- [ ] Set up crash reporting (Firebase Crashlytics)
- [ ] Beta release (internal track)
- [ ] Public release

---

## Git Workflow

### Branches

- `main` — Production ready
- `develop` — Latest development
- `feature/*` — Individual features
- `hotfix/*` — Emergency fixes

### Commit Format

```
type(scope): description

feat(scan): add app permission audit
fix(bluetooth): handle null device name
docs(readme): update feature list
```

---

## Quality Checklist

Before each release:

- [ ] No crashes on test devices
- [ ] All features work as expected
- [ ] UI looks good on different screen sizes
- [ ] Battery usage is acceptable
- [ ] Non-tech user tested and understood
