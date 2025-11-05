# Architecture Documentation

## Overview

Open Fitness Analysis is a minimal Android application designed to demonstrate Health Connect integration. The app follows Android development best practices and uses modern Android libraries.

## Architecture Pattern

This application uses a simplified **MVVM-lite** pattern suitable for a minimal demonstration app:

- **View**: `MainActivity` - Handles UI and user interactions
- **Model**: Health Connect SDK - Data source for fitness activities
- **ViewModel Logic**: Embedded in `MainActivity` using Kotlin Coroutines and Lifecycle scope

For a production app, you would extract business logic into separate ViewModel classes using `androidx.lifecycle.ViewModel`.

## Project Structure

```
com.openfitness.analysis/
├── MainActivity.kt                 # Main activity with Health Connect integration
└── (future classes)                # Room for expansion

res/
├── layout/
│   └── activity_main.xml          # Main UI layout
├── values/
│   ├── strings.xml                # Text resources
│   ├── colors.xml                 # Color definitions
│   ├── themes.xml                 # App theme
│   └── ic_launcher_background.xml # Launcher icon background
├── drawable/
│   └── ic_launcher_foreground.xml # Launcher icon foreground
└── mipmap-anydpi-v26/
    ├── ic_launcher.xml            # Adaptive launcher icon
    └── ic_launcher_round.xml      # Round launcher icon
```

## Key Components

### 1. MainActivity

**Responsibilities:**
- Initialize Health Connect client
- Manage Health Connect permissions
- Read exercise session data
- Display results in the UI

**Key Methods:**
- `onCreate()`: Initialize views and Health Connect client
- `checkPermissions()`: Check current permission status
- `requestPermissionsFromUser()`: Launch permission request flow
- `readActivities()`: Fetch and display exercise data

**Lifecycle:**
- Uses `lifecycleScope` for coroutine management
- Coroutines are automatically cancelled when activity is destroyed

### 2. Health Connect Integration

**SDK Version**: 1.1.0-alpha10

**Permissions Required:**
- `READ_EXERCISE` - Exercise session records
- `READ_ACTIVE_CALORIES_BURNED` - Active calorie data
- `READ_DISTANCE` - Distance data
- `READ_STEPS` - Step count data
- `READ_TOTAL_CALORIES_BURNED` - Total calorie data

**Data Access Flow:**
1. Check SDK availability
2. Create HealthConnectClient instance
3. Request permissions via PermissionController
4. Query records using ReadRecordsRequest
5. Process and display results

**Time Range:**
- Default: Last 30 days
- Configurable via TimeRangeFilter

### 3. UI Components

**Layout**: ConstraintLayout-based responsive design

**Interactive Elements:**
- **Check Permissions Button**: Verify current permission state
- **Request Permissions Button**: Launch system permission dialog
- **Read Activities Button**: Fetch and display exercise data
- **Status TextView**: Show operation status messages
- **Activities ScrollView**: Display exercise session details

**Material Design:**
- Material Components library (1.12.0)
- Material Design 3 theme
- Dark mode support via DayNight theme

## Data Flow

```
User Action → Button Click Listener
            ↓
Coroutine Launch (lifecycleScope)
            ↓
Health Connect Client API Call
            ↓
Suspend Function (async operation)
            ↓
Response Processing
            ↓
UI Update (TextView.setText)
```

## Error Handling

**Approach**: Comprehensive try-catch blocks

**Error Display:**
- Toast messages for critical errors
- Status text for informational messages
- Detailed error messages from exceptions

**Common Errors Handled:**
- Health Connect not available
- Permissions denied
- Network/API failures
- Empty data sets

## Threading Model

**Main Thread:**
- UI updates
- Button click listeners
- Activity lifecycle callbacks

**Background (Coroutines):**
- Health Connect API calls
- Data processing
- Permission checks

**Dispatcher**: Default (Dispatchers.IO for I/O operations)

**Scope**: `lifecycleScope` - automatically cancelled on activity destruction

## Dependencies

### Core Android
- **androidx.core:core-ktx**: Kotlin extensions for Android framework
- **androidx.appcompat:appcompat**: Backward-compatible support library
- **androidx.constraintlayout**: Flexible layout system

### Health Connect
- **androidx.health.connect:connect-client**: Health Connect SDK

### Lifecycle
- **androidx.lifecycle:lifecycle-runtime-ktx**: Lifecycle-aware components
- **androidx.activity:activity-ktx**: Activity extensions with lifecycle support

### UI
- **material:material**: Material Design components

### Testing
- **junit**: Unit testing framework
- **androidx.test.ext:junit**: Android JUnit extensions
- **espresso-core**: UI testing framework

## Build Configuration

**Gradle**: 8.9
**AGP**: 8.5.0
**Kotlin**: 2.0.0

**Compile SDK**: 36 (Android 15)
**Target SDK**: 36
**Min SDK**: 26 (Android 8.0)

**Build Features:**
- View Binding: Enabled
- Minification: Disabled (debug builds)

**ProGuard Rules:**
- Keep Health Connect classes

## Security Considerations

### Permissions

- **Runtime Permissions**: Required for Health Connect access
- **Manifest Declarations**: All health permissions declared
- **Permission Rationale**: Should be implemented for production

### Data Privacy

- **No Data Storage**: App doesn't store health data locally
- **Read-Only Access**: Only reads data, doesn't write
- **User Control**: User can revoke permissions anytime via Settings

### ProGuard

Rules defined to preserve Health Connect classes:
```proguard
-keep class androidx.health.connect.** { *; }
```

## Testing Strategy

### Instrumented Tests

**Location**: `app/src/androidTest/`

**Test Class**: `HealthConnectInstrumentedTest`

**Test Cases:**
1. Health Connect availability check
2. Client creation verification
3. Permission controller access
4. Context integrity validation

**Requirements:**
- Physical device or emulator
- Health Connect installed
- Test runner: AndroidJUnitRunner

### Unit Tests

**Location**: `app/src/test/` (to be implemented)

**Recommended Coverage:**
- Data parsing logic
- Utility functions
- Business logic (when extracted)

## Future Enhancements

### Architecture Improvements
- [ ] Extract ViewModel classes
- [ ] Implement Repository pattern
- [ ] Add Dependency Injection (Hilt/Koin)
- [ ] Use Navigation Component

### Features
- [ ] Write exercise data
- [ ] Aggregate health statistics
- [ ] Data visualization (charts/graphs)
- [ ] Export data functionality
- [ ] Background sync with WorkManager

### Testing
- [ ] Add unit tests for business logic
- [ ] UI tests with Espresso
- [ ] Integration tests
- [ ] Mock Health Connect for tests

### UI/UX
- [ ] Improve Material Design implementation
- [ ] Add loading indicators
- [ ] Implement proper error states
- [ ] Add data filtering options
- [ ] Implement pull-to-refresh

## Performance Considerations

### Current Implementation
- Loads 30 days of data synchronously
- No pagination
- No caching

### Optimization Opportunities
- Implement pagination for large datasets
- Cache recent data locally (with user consent)
- Use Flow for reactive data updates
- Implement incremental loading

## Compatibility

### Android Versions
- **Minimum**: Android 8.0 (API 26)
- **Target**: Android 15 (API 36)
- **Tested**: Android 14+ (with built-in Health Connect)

### Health Connect
- Built-in: Android 14+ (API 34+)
- Via App: Android 9-13 (requires installation from Play Store)

### Screen Sizes
- Phone (portrait/landscape)
- Tablet (with ConstraintLayout responsiveness)

## Known Limitations

1. **No Offline Support**: Requires Health Connect to be available
2. **Limited Error Recovery**: Errors require manual retry
3. **No Data Persistence**: App doesn't cache data locally
4. **Single Activity**: No navigation between screens
5. **Minimal UI**: Basic Material Design implementation

## References

- [Health Connect Guide](https://developer.android.com/health-and-fitness/guides/health-connect)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Android Architecture Components](https://developer.android.com/topic/architecture)
- [Material Design](https://material.io/design)
