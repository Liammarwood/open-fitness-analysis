# Building the Open Fitness Analysis Android App

This document provides detailed instructions for building and running the Open Fitness Analysis Android application.

## Prerequisites

### Required Software

1. **Android Studio** (Hedgehog | 2023.1.1 or later)
   - Download from: https://developer.android.com/studio
   - Includes Android SDK, Gradle, and all necessary tools

2. **Java Development Kit (JDK) 17 or later**
   - Bundled with Android Studio
   - Or download from: https://adoptium.net/

3. **Android SDK**
   - SDK Platform: Android 14 (API 34) minimum, Android 15 (API 36) recommended
   - Build Tools: 34.0.0 or later
   - Install via Android Studio SDK Manager

### Device Requirements

- Android device or emulator running Android 8.0 (API 26) or higher
- **Health Connect** app installed:
  - Built-in on Android 14+ (API 34+)
  - Available via Google Play Store on Android 9-13 (API 28-33)

## Building with Android Studio

### Step 1: Clone the Repository

```bash
git clone https://github.com/Liammarwood/open-fitness-analysis.git
cd open-fitness-analysis
```

### Step 2: Open in Android Studio

1. Launch Android Studio
2. Select **File > Open**
3. Navigate to the cloned repository directory
4. Click **OK**

### Step 3: Sync Gradle

Android Studio will automatically prompt to sync Gradle files. If not:

1. Click **File > Sync Project with Gradle Files**
2. Wait for the sync to complete (may take a few minutes on first run)

### Step 4: Build the Project

Choose one of the following methods:

#### Using Android Studio UI:
- **Build > Make Project** (Ctrl+F9 / Cmd+F9)
- **Build > Build Bundle(s) / APK(s) > Build APK(s)**

#### Using Terminal in Android Studio:
```bash
./gradlew build
```

### Step 5: Run the App

1. Connect an Android device via USB (with USB debugging enabled) or start an emulator
2. Select the device from the device dropdown
3. Click the **Run** button (green triangle) or press Shift+F10
4. The app will install and launch on the selected device

## Building from Command Line

### Using Gradle Wrapper (Recommended)

The project includes Gradle Wrapper scripts that ensure consistent builds:

#### On Linux/macOS:
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run all checks and tests
./gradlew check

# Install on connected device
./gradlew installDebug
```

#### On Windows:
```cmd
# Build debug APK
gradlew.bat assembleDebug

# Build release APK
gradlew.bat assembleRelease

# Run all checks and tests
gradlew.bat check

# Install on connected device
gradlew.bat installDebug
```

### Output Location

After a successful build, APK files are located at:
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release.apk`

## Running Tests

### Instrumented Tests (Requires Device/Emulator)

```bash
# Run all instrumented tests
./gradlew connectedAndroidTest

# Run specific test class
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.openfitness.analysis.HealthConnectInstrumentedTest
```

### Unit Tests (Runs on JVM)

```bash
./gradlew test
```

## Common Issues and Solutions

### Issue: Build fails with "SDK location not found"

**Solution:** Create a `local.properties` file in the project root:

```properties
sdk.dir=/path/to/your/Android/sdk
```

Replace `/path/to/your/Android/sdk` with your actual SDK path:
- **Windows**: `C\:\\Users\\YourName\\AppData\\Local\\Android\\Sdk`
- **macOS**: `/Users/YourName/Library/Android/sdk`
- **Linux**: `/home/YourName/Android/Sdk`

### Issue: "Health Connect not available"

**Solutions:**
1. Install Health Connect from Google Play Store (Android 9-13)
2. Use a device/emulator with Android 14+ (Health Connect is built-in)
3. Ensure the device has Google Play Services

### Issue: Gradle sync fails

**Solutions:**
1. Ensure you have internet connectivity (required for first build)
2. Clear Gradle caches: **File > Invalidate Caches / Restart**
3. Delete `.gradle` and `build` folders, then re-sync

### Issue: Build tools or SDK platform not found

**Solution:** Open SDK Manager in Android Studio:
1. **Tools > SDK Manager**
2. Install Android SDK Platform 36 (or 34 minimum)
3. Install Build-Tools 34.0.0 or later
4. Click **Apply**

## Configuration Options

### Changing Target SDK

Edit `app/build.gradle.kts`:

```kotlin
android {
    compileSdk = 36  // Change to desired SDK version
    
    defaultConfig {
        targetSdk = 36  // Change to desired SDK version
    }
}
```

### Enabling Code Shrinking (Release Builds)

Edit `app/build.gradle.kts`:

```kotlin
android {
    buildTypes {
        release {
            isMinifyEnabled = true  // Enable ProGuard/R8
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

## Signing Release Builds

### Generate Keystore

```bash
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-alias
```

### Configure Signing in `app/build.gradle.kts`

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("path/to/my-release-key.jks")
            storePassword = "your-store-password"
            keyAlias = "my-alias"
            keyPassword = "your-key-password"
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

**Note:** Never commit keystores or passwords to version control!

## Additional Resources

- [Android Developers Guide](https://developer.android.com/guide)
- [Health Connect Documentation](https://developer.android.com/health-and-fitness/guides/health-connect)
- [Gradle Build Tool](https://gradle.org/guides/)
- [Kotlin Language Documentation](https://kotlinlang.org/docs/home.html)

## Support

For issues specific to this project, please open an issue on the GitHub repository:
https://github.com/Liammarwood/open-fitness-analysis/issues
