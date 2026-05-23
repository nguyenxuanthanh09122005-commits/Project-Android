plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.cinema.movie_booking"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.cinema.movie_booking"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            "String",
            "BASE_URL",
            "\"http://192.168.1.20:8080/\""
        )

        buildConfigField(
            "String",
            "IMAGE_URL",
            "\"http://192.168.1.20:8080/uploads/\""
        )

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

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
    implementation(libs.recyclerview)
    implementation(libs.flexbox)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.viewmodel)
}