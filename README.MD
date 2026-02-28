# CMP Bootstrap

A **modular Kotlin Multiplatform (KMP) bootstrap library** designed to provide essential utilities for developing cross-platform Compose Multiplatform applications.

## 🎯 Goal

Our mission is to **eliminate repetitive setup work** for Compose Multiplatform projects. Developers implementing this bootstrap can create production-ready apps **without installing additional packages** for common functionality like DI, networking, navigation, and more.

### Philosophy
- **Modularity First**: Each component is published independently—use only what you need
- **Zero Extra Dependencies**: No unnecessary bloat in your project
- **Consistency Across Projects**: Shared patterns and best practices baked in
- **Easy Updates**: Bug fixes and improvements distribute seamlessly

## 📦 Modules

### 1. **architecture** (`com.mertcaliskanyurek.cmpbootstrap:architecture`)
Core architectural patterns for Compose Multiplatform apps.

**Exports:**
- `ScreenBase` - Base class for all screens with lifecycle management
- `ScreenModelBase` - Base ScreenModel with common functionality
- `NavigationEvent` - Type-safe navigation events
- `Route` - Navigation route abstraction
- `UseCase` - Standard use case pattern for business logic

**Dependencies (built-in):**
- Voyager (navigation & screen models)
- Koin (dependency injection)
- Kotlin Coroutines
- Compose Runtime, UI, Foundation

```kotlin
// example usage
class HomeScreen : ScreenBase() {
    private val viewModel: HomeViewModel by getNavigation()
    
    override fun Content() {
        // Your composable content
    }
}
```

### 2. **networking** (`com.mertcaliskanyurek.cmpbootstrap:networking`)
Multiplatform HTTP client abstraction with sensible defaults.

**Exports:**
- `ApiClient` - Type-safe API client wrapper
- `HttpClientFactory` - Platform-specific HTTP client creation
- `NetworkConfig` - Centralized network configuration

**Features:**
- Ktor client with native platform engines (OkHttp for Android, Darwin for iOS)
- JSON serialization/deserialization with Kotlinx Serialization
- Built-in logging and content negotiation
- Automatic retry logic and timeout handling

**Dependencies:**
- Ktor Client (Core + platform engines)
- Kotlinx Serialization

```kotlin
// example usage
val apiClient = ApiClient.create(NetworkConfig.default)
val users = apiClient.get<List<User>>("/api/users")
```

### 3. **sample** (Demonstration App)
A complete example project showing how to use both modules together with a real Compose Multiplatform setup.

Supports:
- ✅ Android
- ✅ iOS (via Xcode)

## 🚀 Getting Started

### Option 1: Use as Published Libraries (Recommended for New Projects)

**Add to your `build.gradle.kts`:**

```kotlin
dependencies {
    // Use only what you need
    implementation("com.mertcaliskanyurek.cmpbootstrap:architecture:1.0.0")
    implementation("com.mertcaliskanyurek.cmpbootstrap:networking:1.0.0")
}
```

### Option 2: Local Development (Contributing)

**Include in your `settings.gradle.kts`:**

```kotlin
include(":architecture")
include(":networking")
```

**Then reference in your app's `build.gradle.kts`:**

```kotlin
dependencies {
    implementation(project(":architecture"))
    implementation(project(":networking"))
}
```

## 📋 Project Structure

```
CMP-Bootstrap/
├── architecture/                 # Core architectural module
│   ├── src/commonMain/kotlin/   # Shared code
│   └── src/commonTest/kotlin/   # Shared tests
│
├── networking/                   # Networking module
│   ├── src/commonMain/kotlin/   # Shared HTTP abstractions
│   ├── src/androidMain/kotlin/  # Android-specific (OkHttp)
│   └── src/iosMain/kotlin/      # iOS-specific (Darwin)
│
├── sample/
│   ├── composeApp/              # Multiplatform app
│   │   ├── src/androidMain/     # Android app
│   │   ├── src/iosMain/         # iOS app
│   │   └── src/commonMain/      # Shared UI
│   │
│   └── iosApp/                  # Native iOS Xcode project
│
└── gradle/libs.versions.toml    # Centralized dependency management
```

## 🔧 Development

### Building Locally

```bash
# Build all modules
./gradlew build

# Build specific module
./gradlew :architecture:build
./gradlew :networking:build

# Run tests
./gradlew test
./gradlew :architecture:test
```

### Building the Sample App

**Android:**
```bash
./gradlew :sample:composeApp:installDebug
# Or open in Android Studio and run
```

**iOS:**
```bash
cd sample/iosApp
open iosApp.xcodeproj
# Select target and run in Xcode
```

## 📦 Publishing Modules

Each module is independently publishable to Maven Central.

### Prerequisites
1. Create a Maven Central account at [Sonatype](https://central.sonatype.org/register/central-portal/#create-an-account)
2. Generate a GPG key:

```bash
gpg --full-gen-key
gpg --keyserver keyserver.ubuntu.com --send-keys XXXXXXXX
gpg --export-secret-key XXXXXXXX > XXXXXXXX.gpg
```

3. Add to `~/.gradle/gradle.properties`:

```properties
signing.keyId=YOUR_KEY_ID
signing.password=YOUR_KEY_PASSWORD
signing.secretKeyRingFile=/path/to/XXXXXXXX.gpg

mavenCentralUsername=your_username
mavenCentralPassword=your_password
```

### Publishing

```bash
# Publish all modules
./gradlew publishAllPublicationsToMavenCentral

# Publish specific module
./gradlew :architecture:publishAllPublicationsToMavenCentral
./gradlew :networking:publishAllPublicationsToMavenCentral

# For releases
./gradlew publishAllPublicationsToMavenCentral --no-configuration-cache
```

Each module publishes with its own artifact ID:
- `com.mertcaliskanyurek.cmpbootstrap:architecture:1.0.0`
- `com.mertcaliskanyurek.cmpbootstrap:networking:1.0.0`

## 🎓 Usage Examples

### Creating a New Screen

```kotlin
// In your app that depends on :architecture

class LoginScreen : ScreenBase() {
    private val viewModel: LoginViewModel by getScreenModel()
    
    override fun Content() {
        LazyColumn {
            item {
                TextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text("Email") }
                )
            }
            item {
                Button(onClick = { viewModel.login() }) {
                    Text("Login")
                }
            }
        }
    }
}
```

### Making Network Calls

```kotlin
// In your networking layer

class UserRepository(private val apiClient: ApiClient) {
    suspend fun getUser(id: String): User {
        return apiClient.get("/api/users/$id")
    }
    
    suspend fun updateUser(user: User) {
        apiClient.post("/api/users", user)
    }
}

// Initialize in your DI container
single { ApiClient.create(NetworkConfig.default) }
single { UserRepository(get()) }
```

### Navigation

```kotlin
// Type-safe navigation with Voyager
navigator.push(HomeScreen())
navigator.pop()
navigator.popUntil { it is LoginScreen }
```

## 🔄 Dependency Tree

```
Your App
├── :architecture (stable UI patterns)
│   ├── Voyager (navigation)
│   ├── Koin (DI)
│   └── Compose Runtime/UI/Foundation
│
└── :networking (HTTP)
    ├── Ktor Client
    ├── Kotlinx Serialization
    └── Platform engines (OkHttp/Darwin)
```

## 🛣️ Future Roadmap

Planned additions to the bootstrap:

- [ ] **firebase** - Firebase integration (Auth, Firestore, Analytics)
- [ ] **storage** - Local database abstraction (Room/SQLite KMP)
- [ ] **logging** - Structured logging with platform-specific backends
- [ ] **analytics** - Events tracking abstraction
- [ ] **ui-components** - Common Compose UI component library
- [ ] **permissions** - Platform permission handling

Each will be published as a separate, optional module.

## ⚙️ Configuration

### Gradle Properties

**gradle.properties:**
```properties
# Kotlin configuration
kotlin.version=2.0.0
android.useAndroidX=true

# Compose configuration
compose.multiplatformEnabled=true
```

### Dependency Versions

All versions are centralized in `gradle/libs.versions.toml`:

```toml
[versions]
kotlin = "2.0.0"
compose = "1.6.0"
ktor = "2.3.0"
voyager = "1.1.0"
koin = "3.5.0"

[libraries]
kotlin-stdlib = { module = "org.jetbrains.kotlin:kotlin-stdlib", version.ref = "kotlin" }
# ... more
```

## 🤝 Contributing

Contributions are welcome! To contribute:

1. **Fork the repository**
2. **Create a feature branch** (`git checkout -b feature/new-utility`)
3. **Make your changes** with tests
4. **Submit a pull request**

### Contribution Guidelines
- Follow Kotlin style guide
- Add tests for new features
- Update documentation
- Update module versions in `gradle.properties`

## 📝 License

MIT License - See LICENSE file for details

## 👤 Author

**Mert Caliskan Yurek**
- GitHub: [mertcaliskanyurek](https://github.com/mertcaliskanyurek)
- Email: your.email@example.com

## 📚 Resources

- [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-getting-started.html)
- [Voyager Navigation](https://voyager.adriel.cafe/)
- [Koin Dependency Injection](https://insert-koin.io/)
- [Ktor Client](https://ktor.io/docs/client-overview.html)

## ❓ FAQ

**Q: Can I use just the networking module without architecture?**
A: Yes! Each module is independent. Install only what you need.

**Q: Will this bloat my app?**
A: No. You only include modules you actually use, and we keep dependencies minimal.

**Q: How do I update to a new version?**
A: Just update the version number in your `build.gradle.kts`. All modules follow semantic versioning.

**Q: Can I extend these modules?**
A: Absolutely! They're designed to be extended. Create your own subclasses for project-specific needs.

**Q: What's the minimum API level?**
A: Android 21+. iOS 14+.

---

**Last Updated:** February 2026

For issues, feature requests, or questions, please open a GitHub issue.
