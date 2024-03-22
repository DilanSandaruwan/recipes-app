plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.gtp01.group01.android.recipesmobileapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gtp01.group01.android.recipesmobileapp"
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
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
        //noinspection DataBindingWithoutKapt
        dataBinding = true
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    //  ———  Recyclerview ——— //
    implementation(libs.androidx.recyclerview)

    // ——— lifecycle & ViewModel ——— //
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // ——— Android Navigation component ——— //
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //  ——— Dagger - Hilt ——— //
    implementation(libs.hilt.android)
    implementation(libs.legacy.support.v4)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.auth)
    implementation(libs.annotation)
    implementation(libs.core.ktx)
    implementation(libs.androidx.test.ext)
    testImplementation(libs.junit.jupiter)
    ksp(libs.hilt.compiler)

    // ——— AndroidX libraries ———//
    implementation(libs.androidx.window)
    implementation(libs.androidx.preference.ktx)

    //  ———  retrofit ——— //
    implementation(libs.retrofit.core)
    implementation(libs.converter.gson)

    //  ———  coroutines ——— //
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //  ———  Image Library ——— //
    implementation(libs.coil.kt)

    //  ———  Room Library ——— //
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    //  ———  Compose ——— //
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    // ——— ActivityTestRule ——— //
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.runner)

    //  ——— Testing Library ——— //
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.hamcrest)

    // ——— Mockito for Test ——— //
    testImplementation(libs.mockito.core)
    androidTestImplementation(libs.mockito.android)

    implementation(libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.fragment.testing)

    // ——— Room DB Test ——— //
    testImplementation(libs.androidx.room.testing)

    // ——— Coroutine Test ——— //
    implementation(libs.kotlinx.coroutines.test)

    //  ———  Compose Test ——— //
    androidTestImplementation(platform(libs.androidx.compose.bom))

    //services for handling user authentication
    implementation(libs.firebase.ui.auth)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.core)

}