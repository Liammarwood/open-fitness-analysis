# Quick Start Guide

Get up and running with Open Fitness Analysis in 5 minutes!

## Prerequisites Checklist

- [ ] Android Studio installed (Hedgehog or later)
- [ ] JDK 17 or later installed
- [ ] Android device or emulator with Android 8.0+
- [ ] Health Connect app (built-in on Android 14+, or install from Play Store)

## Step-by-Step Setup

### 1. Clone the Repository

```bash
git clone https://github.com/Liammarwood/open-fitness-analysis.git
cd open-fitness-analysis
```

### 2. Open in Android Studio

1. Launch **Android Studio**
2. Click **File > Open**
3. Select the `open-fitness-analysis` folder
4. Click **OK**

### 3. Wait for Gradle Sync

Android Studio will automatically:
- Download Gradle 8.9
- Download project dependencies
- Index the project files

This may take 2-5 minutes on first run.

### 4. Set Up a Device

Choose one of these options:

#### Option A: Use a Physical Device
1. Enable **Developer Options** on your Android device:
   - Go to **Settings > About Phone**
   - Tap **Build Number** 7 times
2. Enable **USB Debugging**:
   - Go to **Settings > Developer Options**
   - Turn on **USB Debugging**
3. Connect device via USB
4. Accept the USB debugging prompt on device

#### Option B: Create an Emulator
1. Click **Device Manager** in Android Studio
2. Click **Create Device**
3. Select a device definition (e.g., Pixel 6)
4. Select Android 14 (API 34) or higher system image
5. Click **Finish**

### 5. Install Health Connect

#### On Android 14+:
Health Connect is built-in! ✅

#### On Android 9-13:
1. Open Google Play Store on device/emulator
2. Search for "Health Connect"
3. Install the app
4. Open Health Connect and complete setup

### 6. Run the App

1. Select your device from the device dropdown in Android Studio
2. Click the **Run** button (green triangle) or press **Shift+F10**
3. Wait for the app to install and launch

### 7. Use the App

Once the app launches:

1. **Click "Request Permissions"**
   - A system dialog will appear
   - Select the permissions to grant
   - Tap **Allow**

2. **Click "Check Permissions"**
   - Verify that permissions were granted
   - Status text will confirm

3. **Click "Read Activities"**
   - The app will fetch exercise data
   - Recent activities will display below

## Troubleshooting

### "Health Connect is not available on this device"

**Solution**: Install Health Connect from Google Play Store or use an emulator with Android 14+.

### "Gradle sync failed"

**Solutions**:
1. Check internet connection
2. Click **File > Invalidate Caches / Restart**
3. Delete `.gradle` folder and re-sync

### "SDK location not found"

**Solution**: Android Studio should auto-configure this. If not, create `local.properties`:

```bash
cp local.properties.example local.properties
# Edit local.properties and set your SDK path
```

### "No activities found"

This is normal! The app reads from Health Connect, which might be empty if:
- Health Connect was just installed
- No fitness apps have logged data
- No manual activities were added

**To test with data:**
1. Open Health Connect app
2. Manually add a test exercise session
3. Return to Open Fitness Analysis
4. Click "Read Activities" again

## Next Steps

Now that the app is running:

- 📖 Read [ARCHITECTURE.md](ARCHITECTURE.md) to understand the code structure
- 🔧 Read [BUILDING.md](BUILDING.md) for advanced build options
- 🧪 Run tests: `./gradlew connectedAndroidTest`
- 🎨 Modify the UI in `app/src/main/res/layout/activity_main.xml`
- 💻 Explore the code in `app/src/main/java/com/openfitness/analysis/MainActivity.kt`

## Testing with Sample Data

Want to see the app in action? Add sample exercise data:

1. Open **Health Connect** app
2. Tap **Data and access**
3. Select **Exercise** > **Add exercise**
4. Fill in details:
   - Activity: Running (or any type)
   - Duration: 30 minutes
   - Date: Today
5. Save
6. Return to **Open Fitness Analysis**
7. Click **Read Activities**

The sample exercise should now appear!

## Learn More

- **Full Documentation**: See [README.md](README.md)
- **Health Connect Guide**: https://developer.android.com/health-and-fitness/guides/health-connect
- **Kotlin Documentation**: https://kotlinlang.org/docs/home.html
- **Android Developers**: https://developer.android.com/

## Getting Help

If you encounter issues:

1. Check the [Troubleshooting](#troubleshooting) section above
2. Review the [BUILDING.md](BUILDING.md) guide
3. Open an issue on GitHub: https://github.com/Liammarwood/open-fitness-analysis/issues

Happy coding! 🚀
