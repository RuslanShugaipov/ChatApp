import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

val apikeyPropertiesFile = rootProject.file("./app/apikey.properties")
val apikeyProperties = Properties()
apikeyProperties.load(apikeyPropertiesFile.inputStream())

android {
    namespace = "com.ruslanshugaipov.chatapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ruslanshugaipov.chatapp"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            "String",
            "WIDGET_TOKEN",
            apikeyProperties["WIDGET_TOKEN"] as String? ?: "null"
        )
        buildConfigField("String", "BASE_HOST", apikeyProperties["BASE_HOST"] as String? ?: "null")
        buildConfigField("String", "WS_HOST", apikeyProperties["WS_HOST"] as String? ?: "null")
        buildConfigField(
            "String",
            "STORAGE_HOST",
            apikeyProperties["STORAGE_HOST"] as String? ?: "null"
        )
        buildConfigField(
            "String",
            "CLIENT_TOKEN",
            apikeyProperties["CLIENT_TOKEN"] as String? ?: "null"
        )

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.chat2desk)

    implementation(libs.coil.compose)

    implementation(libs.kotlinx.datetime)

    androidTestImplementation(platform(libs.androidx.compose.bom))

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}