plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose.compiler)
}

android {
    namespace = "ru.livetyping.zarina.core.ui.kit"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    lint {
        disable += listOf("UsingMaterialAndMaterial3Libraries")
    }
}

kotlin {
    explicitApi()
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("config/compose/stability_config.txt")
}

dependencies {
    api(projects.core.text)
    api(projects.core.uiCompose)
    api(projects.core.uiCommon)

    api(platform(libs.jetpack.compose.bom))
    api(libs.jetpack.compose.ui)
    api(libs.jetpack.compose.material)
    api(libs.jetpack.compose.material3)
    api(libs.jetpack.compose.toolingPreview)
    debugImplementation(libs.jetpack.compose.tooling)
    debugImplementation(libs.jetpack.compose.testManifest)

    lintChecks(libs.lint.composeChecks)

    testImplementation(libs.junit)
    androidTestImplementation(libs.jetpack.test.junit)
    androidTestImplementation(libs.jetpack.espresso)
}
