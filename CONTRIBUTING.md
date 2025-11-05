# Contributing to Open Fitness Analysis

Thank you for your interest in contributing! This document provides guidelines for contributing to the Open Fitness Analysis project.

## Getting Started

1. Fork the repository
2. Clone your fork: `git clone https://github.com/YOUR_USERNAME/open-fitness-analysis.git`
3. Create a branch: `git checkout -b feature/your-feature-name`
4. Make your changes
5. Test your changes
6. Commit and push
7. Open a Pull Request

## Development Setup

### Prerequisites

- Android Studio Hedgehog or later
- JDK 17+
- Android SDK 26+ installed
- Git

### Initial Setup

```bash
# Clone your fork
git clone https://github.com/YOUR_USERNAME/open-fitness-analysis.git
cd open-fitness-analysis

# Add upstream remote
git remote add upstream https://github.com/Liammarwood/open-fitness-analysis.git

# Open in Android Studio
```

## Code Guidelines

### Kotlin Style

Follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html):

- Use 4 spaces for indentation
- Use camelCase for function and variable names
- Use PascalCase for class names
- Maximum line length: 120 characters
- Place opening braces at the end of the line

### Example:

```kotlin
class MyClass {
    private val myProperty: String = "value"
    
    fun myFunction(parameter: Int): String {
        return when (parameter) {
            1 -> "one"
            2 -> "two"
            else -> "other"
        }
    }
}
```

### Android Best Practices

- Use ViewBinding for view access
- Use Kotlin Coroutines for async operations
- Use `lifecycleScope` for lifecycle-aware coroutines
- Handle configuration changes properly
- Follow Material Design guidelines

### Code Organization

```kotlin
class MainActivity : AppCompatActivity() {
    // 1. Constants
    private companion object {
        const val REQUEST_CODE = 100
    }
    
    // 2. Properties
    private lateinit var binding: ActivityMainBinding
    private lateinit var client: HealthConnectClient
    
    // 3. Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        // ...
    }
    
    // 4. Public methods
    fun publicMethod() {
        // ...
    }
    
    // 5. Private methods
    private fun privateMethod() {
        // ...
    }
}
```

## Testing

### Running Tests

```bash
# Run unit tests
./gradlew test

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Run specific test class
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.openfitness.analysis.YourTestClass
```

### Writing Tests

- Place unit tests in `app/src/test/`
- Place instrumented tests in `app/src/androidTest/`
- Use descriptive test names: `testFeatureName_scenario_expectedBehavior`
- Include setup and teardown methods when needed

Example:

```kotlin
@Test
fun readActivities_withValidData_returnsExercises() {
    // Given
    val startTime = Instant.now().minusSeconds(86400)
    val endTime = Instant.now()
    
    // When
    val result = repository.readActivities(startTime, endTime)
    
    // Then
    assertTrue(result.isSuccess)
    assertNotNull(result.data)
}
```

## Commit Messages

Follow the [Conventional Commits](https://www.conventionalcommits.org/) specification:

### Format:

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types:

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

### Examples:

```
feat(health-connect): add support for heart rate data

- Add HeartRateRecord reading capability
- Update UI to display heart rate data
- Add tests for heart rate functionality

Closes #123
```

```
fix(permissions): handle permission denial gracefully

Previously, the app would crash when permissions were denied.
Now it shows a proper error message and allows retry.

Fixes #456
```

## Pull Request Process

### Before Submitting

1. **Update from upstream**:
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Test your changes**:
   ```bash
   ./gradlew check
   ./gradlew connectedAndroidTest
   ```

3. **Update documentation** if needed

4. **Add tests** for new features

### PR Description Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] Unit tests added/updated
- [ ] Instrumented tests added/updated
- [ ] Manual testing completed

## Screenshots (if applicable)
Add screenshots for UI changes

## Checklist
- [ ] Code follows project style guidelines
- [ ] Self-review completed
- [ ] Comments added for complex code
- [ ] Documentation updated
- [ ] No new warnings generated
- [ ] Tests pass locally
```

### Review Process

1. At least one maintainer review required
2. All CI checks must pass
3. Conflicts must be resolved
4. Code must follow style guidelines
5. Tests must be included for new features

## Feature Requests

### How to Request a Feature

1. Check existing issues to avoid duplicates
2. Open a new issue with label `enhancement`
3. Use this template:

```markdown
## Feature Description
Clear description of the feature

## Use Case
Why is this feature needed?

## Proposed Solution
How should this work?

## Alternatives Considered
What other approaches did you consider?

## Additional Context
Screenshots, mockups, or examples
```

## Bug Reports

### How to Report a Bug

1. Check existing issues to avoid duplicates
2. Open a new issue with label `bug`
3. Use this template:

```markdown
## Bug Description
Clear description of the bug

## Steps to Reproduce
1. Step one
2. Step two
3. ...

## Expected Behavior
What should happen?

## Actual Behavior
What actually happens?

## Environment
- Android Version: 
- Device/Emulator: 
- Health Connect Version: 
- App Version: 

## Screenshots
Add screenshots if applicable

## Additional Context
Any other relevant information
```

## Areas for Contribution

Looking to contribute but not sure where to start? Here are some ideas:

### Easy (Good First Issues)

- Improve error messages
- Add UI loading indicators
- Enhance documentation
- Fix typos
- Add more string resources
- Improve layouts for different screen sizes

### Medium

- Add data filtering options
- Implement data export (CSV/JSON)
- Add more health data types (heart rate, sleep, etc.)
- Improve test coverage
- Add data visualization
- Implement caching mechanism

### Advanced

- Extract business logic to ViewModel
- Implement Repository pattern
- Add dependency injection (Hilt)
- Implement background sync with WorkManager
- Add data aggregation features
- Implement multi-screen navigation
- Add widget support

## Code Review Guidelines

### As a Reviewer

- Be respectful and constructive
- Explain the reasoning behind suggestions
- Approve if changes are good enough, not perfect
- Focus on logic and design, not just style
- Test the changes if possible

### As a Contributor

- Respond to all review comments
- Be open to suggestions
- Ask for clarification if needed
- Make requested changes promptly
- Thank reviewers for their time

## Communication

- **GitHub Issues**: Feature requests, bug reports
- **Pull Requests**: Code discussions
- **Commit Messages**: Clear, descriptive change explanations

## License

By contributing, you agree that your contributions will be licensed under the same license as the project (see [LICENSE](LICENSE) file).

## Questions?

If you have questions about contributing:

1. Check existing documentation
2. Search closed issues and PRs
3. Open a new issue with the `question` label

Thank you for contributing! 🎉
