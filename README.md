# Toast
# 🚀 FlashToast Library

A lightweight and customizable **Android Toast Library** with queue support, swipe dismiss, progress bar, and multiple types like **Success, Error, Info, Warning**.

---

## ✨ Features

* ✅ Toast Queue (no overlap)
* 👆 Swipe to dismiss (Left / Right)
* ⏱️ Custom duration support
* 📊 Progress bar indicator
* 🎨 Multiple types:

  * Success
  * Error
  * Info
  * Warning
* 📱 Status bar safe positioning
* 💡 Smooth animations

---

## 📸 Preview

<p align="center">
  <img src="https://github.com/user-attachments/assets/0ea5b937-aff1-4ab5-b2c3-7d0051fa0800" width="22%" />
  <img src="https://github.com/user-attachments/assets/61fad39d-dcdf-43a0-8eb9-8bd1a2733ddd" width="22%" />
  <img src="https://github.com/user-attachments/assets/46986481-507f-48d2-81fe-e7b3f4d253cc" width="22%" />
  <img src="https://github.com/user-attachments/assets/da9b106c-fab2-4841-aec4-9c36f93a379d" width="22%" />
</p>

<p align="center">
  <b>Success</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <b>Error</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <b>Info</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <b>Warning</b>
</p>


---

## 🛠️ Setup

### Option 1: Use as Module

1. Clone the repo:

```bash
git clone https://github.com/Pradum786/Toast.git
```

2. Add module to your project:

```kotlin
implementation(project(":FlashToast"))
```

---

### Option 2: Use `.aar`

1. Build `.aar`:

```bash
./gradlew :FlashToast:assembleRelease
```

2. Copy `.aar` to `libs/` folder

3. Add in `build.gradle`:

```kotlin
repositories {
    flatDir {
        dirs("libs")
    }
}

dependencies {
    implementation(name = "FlashToast-release", ext = "aar")
}
```

---

## 🚀 Usage

```kotlin
FlashToast.success(this, "Operation successful!")
FlashToast.error(this, "Something went wrong!")
FlashToast.info(this, "Here is some info")
FlashToast.warning(this, "Be careful!")
```

### With custom duration

```kotlin
FlashToast.success(this, "Saved!", FlashToast.LONG)
```

---

## 🎯 Toast Types

| Type    | Description            |
| ------- | ---------------------- |
| SUCCESS | Green success message  |
| ERROR   | Red error message      |
| INFO    | Blue info message      |
| WARNING | Yellow warning message |

---

## 📦 Library Structure

```
FlashToast/
 ├── FlashToast.kt
 ├── res/
 │   ├── layout/
 │   ├── values/
```

---

## ⚡ Features Breakdown

### 🔁 Queue System

Ensures only one toast is visible at a time.

### 👆 Swipe Dismiss

* Swipe Left → Dismiss
* Swipe Right → Dismiss

### 📊 Progress Bar

Shows remaining toast duration and pauses on touch.

---

## 🧑‍💻 Author

**Pradum**

---

## 📄 License

MIT License

---

## ⭐ Support

If you like this project, please ⭐ the repo!
