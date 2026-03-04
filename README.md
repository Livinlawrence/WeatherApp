# WeatherApp

A modern, robust Android weather application built with Kotlin and Jetpack Compose, following Clean Architecture principles.

## 🏗 Architecture & Design Considerations

This project follows **Clean Architecture** with a clear separation of concerns across three layers:

-   **Data Layer**: Responsible for data retrieval from network (Retrofit) and local persistence (Room). It implements the repository interfaces defined in the domain layer.
-   **Domain Layer**: Contains the business logic of the application. It includes models, repository interfaces, and use cases. This layer is pure Kotlin and has no dependency on Android frameworks.
-   **UI Layer (Presentation)**: Built with Jetpack Compose, following the **MVVM (Model-View-ViewModel)** pattern. It handles user interactions and observes data from the domain layer.

### General Considerations & Conventions
-   **Dependency Injection**: Managed by Hilt for decoupling and easier testing.
-   **Asynchronous Programming**: Uses Kotlin Coroutines and Flow for reactive data streams and background tasks.
-   **View States**: UI state is managed using `StateFlow` to ensure a single source of truth and lifecycle awareness.
-   **Naming Conventions**: Follows standard Kotlin coding conventions (PascalCase for classes, camelCase for functions/variables).

---

## 🛠 Third-Party Libraries

An exhaustive list of the libraries used and their purpose:

### Core / UI
-   **Jetpack Compose**: Modern toolkit for building native UI.
-   **Material 3**: Latest Material Design components.
-   **Hilt**: Dependency injection specifically tailored for Android.
-   **Hilt Navigation Compose**: For injecting ViewModels into Navigation-based Compose apps.

### Networking & Data
-   **Retrofit**: Type-safe HTTP client for network requests to weather APIs.
-   **OkHttp Logging Interceptor**: For inspecting network traffic during development.
-   **Gson / Moshi**: For parsing JSON responses into Kotlin objects.
-   **Room**: SQLite abstraction for local weather data persistence.

### Utilities
-   **Play Services Location**: For fetching the user's current geographic coordinates.
-   **Secrets Gradle Plugin**: Securely manages API keys from `local.properties`.

### Testing
-   **JUnit 4**: Standard testing framework.
-   **MockK**: Powerful mocking library for Kotlin.
-   **Kotlinx Coroutines Test**: Testing utilities for coroutines (e.g., `runTest`).

---

## 🚀 How to Build

### Prerequisites
- Android Studio Ladybug (or newer).
- JDK 17+.

### Configuration
1.  **Clone the repository**:
    ```bash
    git clone <repository-url>
    ```
2.  **API Keys**:
    The app uses the OpenWeatherMap API. For convenience, a non-critical development key is provided below, but it is recommended to use your own for production.
    
    Open `local.properties` in the root directory and add:
    ```properties
    OPEN_WEATHER_API_KEY=ed602a33481e3703d9804ef53a25c197
    ```

3.  **Sync Gradle**:
    Open the project in Android Studio and wait for the Gradle sync to complete.

### Building & Running
-   To build the project, run: `./gradlew assembleDebug`
-   To run the application, select the `app` configuration and click **Run** in Android Studio.

---

## 🧪 Running Tests
-   **Unit Tests**: `./gradlew test`
-   **Instrumented Tests**: `./gradlew connectedAndroidTest`
