# Gradle Wrapper

The Gradle Wrapper JAR file is missing from this repository for size reasons.

## To Initialize the Wrapper

When you first open this project in Android Studio, it will automatically download and set up the Gradle Wrapper.

Alternatively, if you have Gradle installed locally, run:

```bash
gradle wrapper --gradle-version=8.9
```

This will download the `gradle-wrapper.jar` file to this directory.

## What is the Gradle Wrapper?

The Gradle Wrapper is a small JAR file (approximately 60KB) that bootstraps the Gradle build process. It ensures that:

1. Everyone uses the same Gradle version
2. No separate Gradle installation is required
3. Builds are reproducible across different environments

## Configuration

The wrapper is configured in `gradle-wrapper.properties`:
- Gradle Version: 8.9
- Distribution URL: https://services.gradle.org/distributions/gradle-8.9-bin.zip

## More Information

- [Gradle Wrapper Documentation](https://docs.gradle.org/current/userguide/gradle_wrapper.html)
