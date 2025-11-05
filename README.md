# Open Fitness Analysis

A modern Android application that displays weekly fitness statistics from Health Connect.

## Project Overview

This Android application demonstrates integration with [Health Connect](https://developer.android.com/health-and-fitness/guides/health-connect), Google's unified health and fitness data platform. The app uses Jetpack Compose for a modern UI and displays your weekly fitness stats including steps, distance, calories, and recent workouts.

## Technical Stack

- **Android SDK**: 36 (Latest available)
- **Minimum SDK**: 33 (Android 13)
- **Target SDK**: 36
- **Language**: Kotlin 2.0.21
- **UI Framework**: Jetpack Compose
- **Build Tool**: Gradle 8.13
- **Android Gradle Plugin**: 8.5.0

## Features

- ✅ Health Connect SDK integration
- ✅ Modern Jetpack Compose UI
- ✅ Weekly fitness statistics:
  - Total steps
  - Total distance (km)
  - Total calories burned (kcal)
  - Workout count
- ✅ Recent activities list with details
- ✅ Permission management for health data
- ✅ Material Design 3 theming

## Project Structure

```
open-fitness-analysis/
├── app/
│   ├── build.gradle.kts              # App-level build configuration
│   ├── proguard-rules.pro            # ProGuard rules
│   └── src/
│       ├── androidTest/              # Instrumented tests
│       │   └── java/com/openfitness/analysis/
│       │       └── HealthConnectInstrumentedTest.kt
│       └── main/
│           ├── AndroidManifest.xml   # App manifest with Health Connect permissions
│           ├── java/com/openfitness/analysis/
│           │   └── MainActivity.kt   # Main activity with Health Connect integration
│           └── res/                  # Resources (layouts, values, drawables)
├── build.gradle.kts                  # Project-level build configuration
├── settings.gradle.kts               # Gradle settings
├── gradle.properties                 # Gradle properties
└── .gitignore                        # Git ignore rules for Android projects
```

## Health Connect Permissions

The app requests the following Health Connect permissions:

- `READ_EXERCISE` - Read exercise session records
- `READ_ACTIVE_CALORIES_BURNED` - Read active calories data
- `READ_DISTANCE` - Read distance data
- `READ_STEPS` - Read step count data
- `READ_TOTAL_CALORIES_BURNED` - Read total calories data

## Building the Project

### Prerequisites

- Android Studio Hedgehog or later
- Android SDK 36
- JDK 17 or later
- Health Connect app installed on the test device

### Build Commands

```bash
# Build the project
./gradlew build

# Run instrumented tests (requires a device/emulator with Health Connect)
./gradlew connectedAndroidTest

# Install on a connected device
./gradlew installDebug
```

## Usage

1. Install the app on an Android device with Health Connect (Android 13+)
2. Open the app
3. Click "Grant Permissions" to allow the app to read your health data
4. View your weekly fitness statistics automatically displayed:
   - Total steps for the last 7 days
   - Total distance traveled
   - Total calories burned
   - Number of workouts
5. Scroll down to see a list of your recent activities with details

## Testing

The project includes instrumented tests in `HealthConnectInstrumentedTest.kt` that verify:

- Health Connect SDK availability
- Health Connect client creation
- Permission controller access
- App context integrity

Run tests with:
```bash
./gradlew connectedAndroidTest
```

## Dependencies

### Core Dependencies
- AndroidX Core KTX 1.10.1
- Jetpack Compose BOM 2024.09.00
- Compose Material3
- Compose UI

### Health Connect
- Health Connect Client 1.1.0-alpha10

### Lifecycle
- Lifecycle Runtime KTX 2.6.1
- Activity Compose 1.8.0

### Testing
- JUnit 4.13.2
- AndroidX Test Extensions 1.1.5
- Espresso 3.5.1

## Notes

- Health Connect is built-in on Android 14+ (API 34+)
- For Android 13, Health Connect must be installed from Google Play Store
- The app requires explicit user permission to access health data
- Statistics are calculated for the last 7 days
- The UI uses modern Jetpack Compose with Material Design 3
- Data includes: steps, distance, calories, and exercise sessions

## License

See LICENSE file for details.