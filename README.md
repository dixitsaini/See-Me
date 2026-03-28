# SeeMe - Android Application

SeeMe is a modern Android application built with Jetpack components and Material Design.

## Features
- **Authentication**: Phone-based authentication using Firebase.
- **UI Architecture**: Implements Data Binding and ConstraintLayout for efficient, responsive UI.
- **Theming**: Full support for Day/Night (Dark Mode) using semantic color resources.
- **Navigation**: Uses Jetpack Navigation component for fragment transitions.
- **Persistence**: Integrated with Room Database for local data management.

## Tech Stack
- **Language**: Kotlin
- **UI**: XML with Data Binding / Jetpack Compose
- **Database**: Room
- **Backend**: Firebase (Auth, Messaging)
- **Architecture**: MVVM
- **Libraries**: AppCompat, ConstraintLayout, Navigation Component, Firebase BOM.

## Project Structure
- `app/src/main/java`: Source code (Activities, Fragments, ViewModels, Data).
- `app/src/main/res/values`: String and Color resources (Day mode).
- `app/src/main/res/values-night`: Color resources for Night mode.
- `app/src/main/res/layout`: Layout definitions using ConstraintLayout and Data Binding.

## Recent Updates
- Fixed theme-related crashes by aligning Material/AppCompat themes.
- Refactored UI to use `ConstraintLayout` for better performance.
- Moved hardcoded strings to `strings.xml`.
- Implemented a unified color palette for Light and Dark themes.
