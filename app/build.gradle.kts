plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

@Suppress("UnstableApiUsage")
android {
    compileSdk = 33

    defaultConfig {
        applicationId = "net.ouftech.oufmime"
        minSdk = 26
        targetSdk = 33
        versionCode = 3
        versionName = "1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    flavorDimensions += listOf("personalization")
    productFlavors {
        create("stock") {
            dimension = "personalization"
        }
        create("custom") {
            dimension = "personalization"
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
        kotlinCompilerExtensionVersion = "1.4.4"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "net.ouftech.oufmime"
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.window:window:1.0.0")

    // Room
    val roomVersion = "2.5.0"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // Koin
    val koinVersion = "3.3.3"
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-androidx-compose:3.4.2")
    testImplementation("io.insert-koin:koin-test-junit4:$koinVersion")

    // Compose
    val composeBom = platform("androidx.compose:compose-bom:2023.03.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation("androidx.navigation:navigation-compose:2.5.3")

    // Tests
    testImplementation("io.mockk:mockk-android:1.13.4")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("junit:junit:4.13.2")
    testImplementation("pl.pragmatists:JUnitParams:1.1.1")
}