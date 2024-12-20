plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
}

android {
    namespace = "com.victor.projectwhatsapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.victor.projectwhatsapp"
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
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    //Dependencies Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))


    implementation("com.google.firebase:firebase-analytics") //Google Analytics firebase
    implementation("com.google.firebase:firebase-auth-ktx") //Auth Firebase
    implementation("com.google.firebase:firebase-firestore-ktx") //CloudFireStore Firebase Database
    implementation("com.google.firebase:firebase-storage-ktx") //CloudStorage

    //Picasso
    implementation("com.squareup.picasso:picasso:2.8")



    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}