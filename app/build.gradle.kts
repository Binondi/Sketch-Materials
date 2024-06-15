plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "devs.org.sketch.material"
    compileSdk = 34

    defaultConfig {
        applicationId = "devs.org.sketch.material"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures{ viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.intuit.sdp:sdp-android:1.1.1")
    //noinspection UseTomlInstead
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    //noinspection UseTomlInstead
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    //noinspection UseTomlInstead
    implementation("com.airbnb.android:lottie:6.2.0")
    //noinspection UseTomlInstead
    implementation("androidx.browser:browser:1.8.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
}