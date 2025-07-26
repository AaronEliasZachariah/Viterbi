# Viterbi - Multiplatform Note Taking App

A modern note-taking application built with Kotlin Multiplatform and Compose Multiplatform, designed to work seamlessly across Android and web platforms.

## 🚀 Tech Stack

- **Kotlin Multiplatform (KMP)** - Shared business logic
- **Compose Multiplatform** - Unified UI framework
- **Kotlinx Serialization** - JSON serialization
- **Kotlinx Coroutines** - Asynchronous programming
- **SQLDelight** - Cross-platform database
- **Koin** - Dependency injection

## 📱 Platforms

- ✅ Android
- ✅ Web (Desktop browsers)

## 🏗️ Project Structure

```
Viterbi/
├── shared/                 # Shared Kotlin code
│   ├── commonMain/        # Common business logic
│   ├── androidMain/       # Android-specific code
│   └── jsMain/           # Web-specific code
├── androidApp/            # Android application
└── webApp/               # Web application
```

## 🎯 Learning Goals

As we build this app, you'll learn:

1. **Kotlin Multiplatform fundamentals**
2. **Compose Multiplatform UI development**
3. **Cross-platform data persistence**
4. **State management in multiplatform apps**
5. **Platform-specific implementations**
6. **Modern Android and web development**

## 🚀 Getting Started

We'll build this step by step, one commit at a time. Each commit will introduce new concepts and features.

### Current Status: Persistent Storage & Navigation
- ✅ Room database for Android persistent storage
- ✅ LocalStorage for Web persistent storage
- ✅ Note detail/editing screen
- ✅ Error handling and loading states
- ✅ Navigation between list and detail screens

---

*I used AI to generate this README* 
