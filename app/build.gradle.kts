plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.cookbook_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cookbook_app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures{
        viewBinding =true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation ("com.squareup.picasso:picasso:2.8")
    implementation ("com.google.code.gson:gson:2.10.1")


    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation ("androidx.cardview:cardview:1.0.0")


    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("com.github.ivbaranov:materialfavoritebutton:0.1.5")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}