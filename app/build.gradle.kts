import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}

val properties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

fun getProperties(key: String): String = properties[key].toString()

android {
    namespace = "kr.cosine.nbatracker"
    compileSdk = 36

    defaultConfig {
        applicationId = "kr.cosine.nbatracker"
        minSdk = 26
        targetSdk = 36
        versionCode = 10
        versionName = "1.3.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(getProperties("key-store-file-path"))
            storePassword = getProperties("key-store-password")
            keyAlias = getProperties("key-store-alias")
            keyPassword = getProperties("key-store-password")
        }
    }

    buildTypes {
        release {
            ndk {
                debugSymbolLevel = "FULL"
            }
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    implementation("androidx.activity", "activity-compose", "1.9.0")
    implementation("androidx.lifecycle", "lifecycle-viewmodel-compose", "2.7.0")
    implementation("androidx.compose.material3", "material3", "1.2.1")
    implementation("androidx.lifecycle", "lifecycle-runtime-compose", "2.7.0")
    implementation("io.coil-kt", "coil-compose", "2.4.0")
    implementation("io.coil-kt", "coil-svg", "2.4.0")
    implementation("org.jsoup", "jsoup", "1.15.3")
    implementation("com.github.skydoves", "cloudy", "0.1.2")
    implementation("com.googlecode.json-simple", "json-simple", "1.1.1") {
        exclude("org.hamcrest")
    }
    /*
    implementation(platform("androidx.compose:compose-bom:2024.04.01"))
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material-icons-extended:1.6.2")
    implementation("com.google.accompanist:accompanist-pager:0.34.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.34.0")
    */
    testImplementation("junit", "junit", "4.13.2")
    androidTestImplementation("androidx.test.ext", "junit", "1.1.5")
    androidTestImplementation("androidx.test.espresso", "espresso-core", "3.5.1")
    androidTestImplementation("androidx.compose.ui", "ui-test-junit4")
    debugImplementation("androidx.compose.ui", "ui-tooling")
    debugImplementation("androidx.compose.ui", "ui-test-manifest")
}