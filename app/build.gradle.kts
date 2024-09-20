plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.javath"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.javath"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation ("net.sourceforge.jtds:jtds:1.3.1")
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation(libs.appcompat)
        implementation(libs.material)
        implementation(libs.constraintlayout)
        implementation(libs.navigation.fragment)
        implementation(libs.navigation.ui)
        implementation(libs.activity)
        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)
        implementation ("androidx.recyclerview:recyclerview:1.2.1")
        implementation (files("libs/mssql-jdbc-12.8.1.jre8.jar"))


}


