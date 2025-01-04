plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") //version "2.0.21-1.0.27"
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.marcohuijskes.runningapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.marcohuijskes.runningapp"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Material Design
    implementation(libs.material)


    // RecyclerView
    implementation(libs.androidx.recyclerview)

    // JSON
    implementation(libs.gson)

    // Coil Image to Bitmap converter
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Room database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)
    // optional - RxJava2 support for Room
    // implementation("androidx.room:room-rxjava2:$room_version")
    // optional - RxJava3 support for Room
    // implementation("androidx.room:room-rxjava3:$room_version")
    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation(libs.androidx.room.guava)
    // optional - Test helpers
    testImplementation(libs.androidx.room.testing)
    // optional - Paging 3 Integration
    implementation(libs.androidx.room.paging)

    // Others

    // Lifecycle
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.extensions)
    implementation (libs.androidx.lifecycle.runtime)
    implementation (libs.androidx.lifecycle.runtime.ktx)
    //implementation("android.arch.lifecycle:extensions:1.1.1") // LifeCycle Extensions (for service)

    // Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Glide
    implementation(libs.glide)
    ksp(libs.ksp)

    // Activity KTX for viewModels()
    implementation (libs.androidx.activity.ktx)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    //Easy Permissions
    //implementation(libs.easypermissions)
    //implementation("com.vmadalin:easypermissions-ktx:1.0.0")

    //KPermissions
    implementation("com.github.fondesa:kpermissions:3.5.0")
    implementation("com.github.fondesa:kpermissions-coroutines:3.5.0")

    // MPAndroidChart - Line chart
    implementation(libs.mpandroidchart)


    // Timber - Better logging
    implementation (libs.timber)

    // Google Play Services
    // Google Maps Location Services
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)


    // Firebase
    // Firebase Firestore
    implementation (libs.firebase.firestore)
    // Firebase Storage KTX
    implementation (libs.firebase.storage.ktx)
    // Firebase Coroutines
    implementation (libs.kotlinx.coroutines.play.services)

    // ExoPlayer
    api (libs.exoplayer.core)
    api (libs.exoplayer.ui)
    api (libs.extension.mediasession)
}