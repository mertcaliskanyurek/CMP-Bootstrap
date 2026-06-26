plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvmToolchain(17)

    androidTarget { publishLibraryVariants("release") }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(libs.voyager.koin)
            api(libs.voyager.screenmodel)
            api(libs.coroutines.core)
            api(compose.runtime)
            api(compose.ui)
            api(compose.foundation)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.coroutines.test)
        }

    }

    sourceSets.all {
        languageSettings.enableLanguageFeature("ExplicitBackingFields")
    }

    //https://kotlinlang.org/docs/native-objc-interop.html#export-of-kdoc-comments-to-generated-objective-c-headers
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations["main"].compileTaskProvider.configure {
            compilerOptions {
                freeCompilerArgs.add("-Xexport-kdoc")
            }
        }
    }

}

android {
    namespace = "com.mertcaliskanyurek.cmpbootstrap"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }
}

//Publishing your Kotlin Multiplatform library to Maven Central
//https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-publish-libraries.html
mavenPublishing {
    publishToMavenCentral()
    coordinates("com.mertcaliskanyurek.cmpbootstrap", "architecture", "1.0.0")

    pom {
        name = "CmpBootstrap"
        description = "Kotlin Multiplatform library"
        url = "https://github.com/mertcaliskanyurek/CMP-Bootstrap"

        licenses {
            license {
                name = "MIT"
                url = "https://opensource.org/licenses/MIT"
            }
        }

        developers {
            developer {
                id = "mertcaliskanyurek"
                name = "Mert Caliskan Yurek"
                email = "your.email@example.com"
            }
        }

        scm {
            url = "https://github.com/mertcaliskanyurek/CMP-Bootstrap"
        }
    }
    if (project.hasProperty("signing.keyId")) signAllPublications()
}
