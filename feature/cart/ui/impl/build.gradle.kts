plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.compose.compiler)
}

android {
    namespace = "ru.livetyping.zarina.feature.cart.ui.impl"
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

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("config/compose/stability_config.txt")
}

dependencies {
    implementation(projects.feature.cart.ui.api)
    implementation(projects.core.uiKit)
    implementation(projects.core.uiModel)
    implementation(projects.core.coroutinesUtil)
    implementation(projects.core.navigationUtil)
    implementation(projects.core.resource)

    implementation(libs.jetpack.lifecycle.runtime.compose)
    implementation(libs.jetpack.lifecycle.viewModel.compose)
    implementation(libs.jetpack.navigation.compose)

    implementation(platform(libs.jetpack.compose.bom))
    implementation(libs.jetpack.compose.ui)
    implementation(libs.jetpack.compose.material)
    implementation(libs.jetpack.compose.material.navigation)
    implementation(libs.jetpack.compose.material3)
    implementation(libs.jetpack.compose.toolingPreview)
    debugImplementation(libs.jetpack.compose.tooling)
    debugImplementation(libs.jetpack.compose.testManifest)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigationCompose)

    implementation(libs.timber)

    lintChecks(libs.lint.composeChecks)

    testImplementation(libs.junit)
    androidTestImplementation(libs.jetpack.test.junit)
    androidTestImplementation(libs.jetpack.espresso)
}
