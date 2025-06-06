plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.cs4084_finalproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cs4084_finalproject"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.preference)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.fragment:fragment-ktx:1.8.6")
    implementation("com.android.volley:volley:1.2.1")
    implementation("androidx.core:core-ktx:1.13.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.google.android.material:material:1.11.0")

    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")


    val work_version = "2.9.0"

    implementation("androidx.work:work-runtime:$work_version")
    implementation("androidx.work:work-runtime:$work_version")


}