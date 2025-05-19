@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.library")
}

android {
    namespace = "com.nfynt.digilensvoiceuiunityplugin"
    compileSdk = 34

    defaultConfig {
        minSdk = 19
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

// Custom tasks
tasks.register<Delete>("deleteExistingAAR") {
    delete("build/release/digilensvoiceuiunityplugin.aar")
}

tasks.register<Copy>("exportAAR") {
    from("build/outputs/aar/")
    into("build/release/")
    include("app-release.aar")
    rename("app-release.aar", "digilensvoiceuiunityplugin.aar")
    dependsOn("deleteExistingAAR", "build")
}

dependencies {
//    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
//    implementation(mapOf("name" to "digios_voiceui_api_1_1_5", "ext"  to "aar"))
    implementation(files("libs/digios_voiceui_api_1_1_5.aar"))
    implementation(libs.appcompat)
    implementation(libs.material)
    androidTestImplementation(libs.espresso.core)
//    libs/digios_voiceui_api_1_1_5.aar
}