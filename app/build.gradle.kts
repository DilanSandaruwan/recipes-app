plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
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
        //noinspection DataBindingWithoutKapt
        dataBinding = true
        viewBinding = true
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
// Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    //noinspection UseTomlInstead
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.firebaseui:firebase-ui-auth:8.0.2")

    // Required only if Facebook login support is required
    // Find the latest Facebook SDK releases here: https://github.com/facebook/facebook-android-sdk/blob/master/CHANGELOG.md
    implementation ("com.facebook.android:facebook-login:8.1.0")


    implementation ("com.google.firebase:firebase-auth:22.3.1")
    implementation ("com.google.firebase:firebase-core:21.1.1")
}