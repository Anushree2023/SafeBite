plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "anu.trial.safebite" // Replace with your actual package name (e.g., com.example.image_ai_app)
    compileSdk = 35 // Or the latest SDK version you are targeting

    defaultConfig {
        applicationId = "anu.trial.safebite" // Same as namespace
        minSdk = 24 // Or your minimum supported SDK version
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false // For development, keep it false. Enable for release builds.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17 // Or the Java version compatible with your project
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17" // Or the Java version compatible with your project
    }
    buildFeatures {
        viewBinding = true // If you are using ViewBinding (recommended for cleaner code)
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0") // Latest core-ktx
    implementation("androidx.appcompat:appcompat:1.7.0-alpha02") // Latest AppCompat
    implementation("com.google.android.material:material:1.11.0-alpha02") // Latest Material
    implementation("androidx.constraintlayout:constraintlayout:2.2.0-alpha13") // Latest ConstraintLayout

    // Retrofit for networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.0") // For String response

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.google.mlkit:text-recognition:16.0.0-beta6")

    // CameraX Core Library
    implementation ("androidx.camera:camera-core:1.3.0")

    // CameraX Lifecycle (Manages camera lifecycle automatically)
    implementation ("androidx.camera:camera-lifecycle:1.3.0")

    // CameraX View (For easy preview handling)
    implementation ("androidx.camera:camera-view:1.3.0")

    // CameraX Extensions (For advanced features like HDR, Night mode, etc.)
    implementation ("androidx.camera:camera-extensions:1.3.0")

    implementation ("com.google.ai.client.generativeai:generativeai:0.4.0")// Latest Gemini API SDK
    implementation ("com.google.code.gson:gson:2.10")// JSON parsing (Optional)

    implementation ("com.squareup.retrofit2:converter-gson:2.9.0") // Convert JSON responses

}
