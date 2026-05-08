import com.android.build.gradle.ProguardFiles.getDefaultProguardFile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.dagger.hilt.android") version "2.48"
    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.heartbeat"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.heartbeat"
        minSdk = 33
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    androidResources {
        generateLocaleConfig = false
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
        disable += "RememberInComposition"
    }

    hilt {
        enableAggregatingTask = false
    }

    bundle {
        language {
            enableSplit = false
        }
    }
}

dependencies {
    // AndroidX core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation(libs.androidx.activity.compose)
    debugImplementation("androidx.compose.ui:ui-tooling")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Testing
    testImplementation(libs.junit)
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Date time
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

    // Icon
    implementation("androidx.compose.material:material-icons-extended")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Accompanist (new version)
    implementation("com.google.accompanist:accompanist-permissions:0.36.0")
    implementation("com.google.accompanist:accompanist-pager:0.36.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.36.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    ksp("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Firebase BOM + KTX
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Firebase Storage
    implementation("com.google.firebase:firebase-storage-ktx")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.36.0")

    // Loading network image
    implementation("io.coil-kt:coil-compose:2.7.0")

    // TESTING
    // Framework chính để chạy Test
    testImplementation("junit:junit:4.13.2")

    // MockK: Thư viện Mock dữ liệu tốt nhất cho Kotlin (Dùng để giả lập Firebase, Repository)
    testImplementation("io.mockk:mockk:1.13.8")

    // Kiểm thử Coroutines (vì project của bạn dùng suspend functions trong UseCase)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Turbine: Thư viện cực hay để test Flow (nếu bạn có dùng StateFlow/SharedFlow)
    testImplementation("app.cash.turbine:turbine:1.0.0")

    // Google Truth: Giúp các câu lệnh assert (khẳng định) đọc giống ngôn ngữ tự nhiên hơn
    testImplementation("com.google.truth:truth:1.1.5")

    // BOM cho Compose testing
    val composeBom = platform("androidx.compose:compose-bom:2024.09.01")
    androidTestImplementation(composeBom)

    // Thư viện chính để tìm kiếm component và tương tác (click, type) trên UI
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Cần thiết để chạy được UI Test trên máy ảo/máy thật
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Test Navigation: Kiểm tra xem nhấn nút có chuyển màn hình đúng không
    androidTestImplementation("androidx.navigation:navigation-test:2.7.7")

    // Hilt Testing: Để Inject các thành phần giả lập vào UI Test
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")
}