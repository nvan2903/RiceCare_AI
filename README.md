<div align="center">

# RiceCare AI

### AI-powered Android app for rice leaf disease diagnosis and contextual farming support

![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-0A7EA4)
![Firebase](https://img.shields.io/badge/Auth-Firebase-FFCA28?logo=firebase&logoColor=black)

Built as an end-to-end mobile product that combines image-based plant disease detection, Firebase authentication, API-driven diagnosis history, and AI chat consultation in a modern Android stack.

</div>

## Why This Project Stands Out

RiceCare AI is more than a UI showcase. It is a practical Android product built around a real agriculture use case: helping users detect rice leaf diseases from images and continue into an AI-guided consultation flow.

From a portfolio perspective, this repository demonstrates the ability to ship a multi-screen mobile application with real backend integration, state management, authentication, file upload, and streaming responses.

## Highlights

| Area | What it demonstrates |
| --- | --- |
| Product problem | Applies mobile engineering to an agriculture and computer vision workflow |
| Mobile architecture | MVVM with Compose, Navigation Compose, ViewModels, and state-driven UI |
| Backend integration | Retrofit-based API layer for auth, predictions, history, chat, and profile data |
| Authentication | Google Sign-In with Firebase Authentication |
| User workflow | Upload image, get prediction, inspect disease info, continue with contextual AI chat |
| Portfolio value | Real use case, non-trivial app flow, and production-like client responsibilities |

## Core Features

- Upload rice leaf images for disease prediction
- View prediction confidence and disease information
- Continue into AI chat based on a diagnosis result
- Track prediction history with detail and delete flows
- Sign in with Google via Firebase Authentication
- Review dashboard statistics and account-related data
- Consume streaming chat responses from backend endpoints

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- MVVM architecture
- Navigation Compose
- Retrofit
- OkHttp
- Gson
- Kotlin Coroutines
- StateFlow
- Firebase Authentication
- Firebase Firestore
- Firebase Storage
- Firebase Analytics
- Coil
- Gradle Kotlin DSL

## App Flow

1. User signs in with Google.
2. User uploads a rice leaf image.
3. Backend returns prediction data with confidence and disease context.
4. User reviews the diagnosis result.
5. User opens an AI chat tied to that diagnosis.
6. User can revisit previous results through prediction history.

## Main Modules

```text
app/src/main/java/com/example/ricecare_ai/
|- data/
|  |- api/          # Retrofit service contracts and network access
|  |- model/        # API response and domain-facing data models
|- navigation/      # Route definitions and app navigation graph
|- ui/              # Jetpack Compose screens and presentation layer
|- viewmodel/       # UI state management and business flow orchestration
```

## Screens Included

- Splash
- Login
- Dashboard
- Upload and prediction result
- AI chat consultation
- Prediction history and detail
- Settings and account screens

## API Surface Covered

The app already integrates a relatively broad backend surface for a personal mobile project:

- Authentication and token verification
- Disease prediction and prediction history
- Chat conversations and streaming messages
- User profile and dashboard statistics
- Disease information lookup
- Health check endpoint

This gives the repository stronger engineering credibility than a static demo app because it reflects real client-server responsibilities.

## Local Setup

### Requirements

- Android Studio Hedgehog or newer
- JDK 11
- Android SDK with `compileSdk 36`
- Firebase project configured for Google Sign-In
- Running RiceCare backend API

### Run Locally

1. Clone the repository.
2. Open the project in Android Studio.
3. Add a valid `google-services.json` file inside `app/`.
4. Review the backend base URL in `app/build.gradle.kts`.
5. Sync Gradle.
6. Run the `app` configuration on an emulator or Android device.

### Firebase Setup

The repository includes `app/google-services.json.example` as a placeholder template. Replace it with your actual Firebase configuration before launching the app.

## What This Project Demonstrates

- Building a complete Android app with a modern Compose-based UI
- Structuring a mobile codebase using MVVM and clear module separation
- Working with authenticated APIs and remote data flows
- Designing user flows that connect computer vision output to downstream product features
- Handling richer client behavior such as history management and streaming chat responses

## Next High-Impact Improvements

- Add polished screenshots or a short demo GIF at the top of the README
- Move API base URLs to environment-specific local configuration
- Add ViewModel and API-layer tests
- Add CI for lint and debug build checks
- Add a short roadmap or release history

## Recruiter Snapshot

If a recruiter opens this repository, the intended takeaway is straightforward: this is a serious Android project with a real domain problem, real integrations, and enough product depth to show engineering maturity beyond tutorial-level work.