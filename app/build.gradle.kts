plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// GitHub Actions 가 빌드 번호를 -PvCode 로 넘겨 줌 (버전이 하나씩 올라감)
val vCode = (project.findProperty("vCode") as String?)?.toIntOrNull() ?: 1

android {
    namespace = "com.onyu.study"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.onyu.study"
        minSdk = 26
        targetSdk = 34
        versionCode = vCode
        versionName = "1.0.$vCode"
    }

    // 업데이트가 덮어 설치되려면 항상 같은 열쇠로 서명해야 함
    signingConfigs {
        create("release") {
            storeFile = file("release.keystore")
            storePassword = "studyapp"
            keyAlias = "study"
            keyPassword = "studyapp"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }
    buildFeatures { buildConfig = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
}
