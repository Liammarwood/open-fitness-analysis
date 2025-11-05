# Open Fitness Analysis

A minimal Android application that reads activities and exercises logged in Health Connect.

## Project Overview

This Android application demonstrates integration with [Health Connect](https://developer.android.com/health-and-fitness/guides/health-connect), Google's unified health and fitness data platform. The app requests permissions to read exercise data and displays previously logged activities.

## Technical Stack

- **Android SDK**: 36 (Latest available)
- **Minimum SDK**: 26 (Android 8.0)
- **Target SDK**: 36
- **Language**: Kotlin 2.0.0
- **Build Tool**: Gradle 8.9
- **Android Gradle Plugin**: 8.5.0

## Features

- ✅ Health Connect SDK integration
- ✅ Permission management for health data
- ✅ Read exercise sessions from the last 30 days
- ✅ Display exercise details (type, duration, start/end time)
- ✅ Material Design UI
- ✅ Instrumented tests for Health Connect functionality

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

1. Install the app on an Android device with Health Connect
2. Open the app
3. Click "Request Permissions" to grant Health Connect permissions
4. Click "Check Permissions" to verify granted permissions
5. Click "Read Activities" to load and display exercise data from the last 30 days

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
- AndroidX Core KTX 1.15.0
- AndroidX AppCompat 1.7.0
- Material Components 1.12.0
- ConstraintLayout 2.2.0

### Health Connect
- Health Connect Client 1.1.0-alpha10

### Lifecycle
- Lifecycle Runtime KTX 2.8.7
- Activity KTX 1.9.3

### Testing
- JUnit 4.13.2
- AndroidX Test Extensions 1.2.1
- Espresso 3.6.1

## Notes

- Health Connect must be installed on the device (available on Android 13+ or via Google Play on Android 9-12)
- The app requires explicit user permission to access health data
- Exercise data is read from the last 30 days
- The UI displays exercise type, duration, and timestamps

## License

See LICENSE file for details.