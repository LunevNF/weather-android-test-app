plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.sibtex.weather_android_test_app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sibtex.weather_android_test_app"
        minSdk = 29
        targetSdk = 36
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
}

// Задача для завершения процессов Java, блокирующих файлы сборки
//tasks.register("killJavaProcesses") {
//    doLast {
//        // Завершаем процессы Java, которые могут блокировать файлы
//        try {
//            val process = ProcessBuilder("taskkill", "/F", "/IM", "java.exe")
//                .redirectErrorStream(true)
//                .start()
//            process.waitFor()
//        } catch (e: Exception) {
//            // Игнорируем ошибки, если процесс не найден
//        }
//
//        // Также завершаем процессы Gradle daemon
//        try {
//            val process = ProcessBuilder("taskkill", "/F", "/IM", "gradle.exe")
//                .redirectErrorStream(true)
//                .start()
//            process.waitFor()
//        } catch (e: Exception) {
//            // Игнорируем ошибки
//        }
//
//        // Небольшая задержка, чтобы процессы успели завершиться
//        Thread.sleep(1000)
//    }
//}

// Настраиваем зависимости после того, как все задачи будут созданы
//afterEvaluate {
//    // Запускаем killJavaProcesses перед сборкой
//    tasks.findByName("clean")?.dependsOn("killJavaProcesses")
//    tasks.findByName("assembleDebug")?.dependsOn("killJavaProcesses")
//    tasks.findByName("assembleRelease")?.dependsOn("killJavaProcesses")
//    tasks.findByName("build")?.dependsOn("killJavaProcesses")
//
//    // Также перед обработкой ресурсов
//    tasks.findByName("processDebugResources")?.dependsOn("killJavaProcesses")
//    tasks.findByName("processReleaseResources")?.dependsOn("killJavaProcesses")
//}

kapt {
    correctErrorTypes = true
    javacOptions {
        option("-encoding", "UTF-8")
    }
    arguments {
        arg("dagger.generateProguardRules", "false")
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.material)
    
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    
    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.google.material)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    
    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)
    
    // DataStore
    implementation(libs.datastore.preferences)
    
    // ViewModel
    implementation(libs.viewmodel.ktx)
    implementation(libs.viewmodel.compose)
    
    // Gson
    implementation(libs.gson)
    
    // Image Loading
    implementation(libs.coil.compose)
    
    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    
    // Glide
    implementation(libs.glide)
    
    // Lifecycle
    implementation(libs.lifecycle.livedata)
    
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}