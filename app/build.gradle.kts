plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.bevmate"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bevmate"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.analytics.impl)
    implementation(libs.recyclerview)
    implementation(libs.coordinatorlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.glide)
    annotationProcessor(libs.glideCompiler)

    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "androidx.lifecycle" && requested.name == "lifecycle-common") {
                useVersion("2.6.2")
            }
            if (requested.group == "org.jetbrains.kotlinx" && requested.name == "kotlinx-coroutines-play-services") {
                useVersion("1.7.3")
            }
            if (requested.group == "androidx.fragment" && requested.name == "fragment-ktx") {
                useVersion("1.6.2")
            }
            if (requested.group == "com.google.android.gms" && requested.name == "play-services-tasks") {
                useVersion("18.0.2")
            }
        }
    }

}