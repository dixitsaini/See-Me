# SeeMe - Android Application

SeeMe is a modern Android application built with Jetpack components and Material Design.

## Features
- **Authentication**: Phone-based authentication using Firebase (Phone & Email).
- **Responsive UI**: Integrated **SDP** and **SSP** for consistent scaling across devices.
- **UI Architecture**: Data Binding, ConstraintLayout, and Base Classes for efficient development.
- **Theming**: Full support for Day/Night (Dark Mode) using semantic color resources.
- **Navigation**: Jetpack Navigation component with fragmented transitions.
- **Persistence**: Room Database for local data and **PreferenceHelper** abstraction for SharedPreferences.

## Tech Stack
- **Language**: Kotlin
- **Architecture**: MVVM with Base Classes (BaseActivity, BaseFragment).
- **UI Libraries**: AppCompat, ConstraintLayout, Material Components, SDP, SSP.
- **Database**: Room
- **Backend**: Firebase Auth, Firebase Cloud Messaging (FCM).

## Project Structure
- `app/src/main/java/com/example/seeme/activity`: Base and specialized activities.
- `app/src/main/java/com/example/seeme/ui/fragments`: Fragment hierarchy including Registration, OTP, and Country Selection.
- `app/src/main/java/com/example/seeme/data`: Data persistence (Room, PreferenceHelper).
- `app/src/main/res/layout`: Optimized XML layouts using scalable units.

## Recent Updates
- **Base Architecture**: Implemented `BaseActivity` and `BaseFragment` to centralize generic logic (Keyboard, Edge-to-Edge).
- **Scalability**: Replaced static `dp`/`sp` with `sdp`/`ssp` for multi-device support.
- **Security**: Hardened `.gitignore` and removed sensitive API keys from version control.
- **Abstraction**: Added `PreferenceHelper` singleton to decouple preference logic from UI.
- **Optimization**: Unified color palette and centralized all strings for internationalization.
