plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.livetyping.zarina.feature.search.ui.impl"
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
    stabilityConfigurationFiles.add(rootProject.layout.projectDirectory.file("config/compose/stability_config.txt"))
}

dependencies {
    implementation(projects.feature.search.ui.api)
    implementation(projects.core.uiCompose)
    implementation(projects.core.uiKit)
    implementation(projects.core.uiKitPaging)
    implementation(projects.core.uiModel)
    implementation(projects.core.uiComponent)
    implementation(projects.core.coroutinesUtil)
    implementation(projects.core.resource)
    implementation(projects.core.kotlinUtil)
    implementation(projects.core.navigationUtil)

    implementation(libs.jetpack.lifecycle.runtime.compose)
    implementation(libs.jetpack.lifecycle.viewModel.compose)
    implementation(libs.jetpack.paging.compose)

    implementation(platform(libs.jetpack.compose.bom.beta))
    implementation(libs.jetpack.compose.ui)
    implementation(libs.jetpack.compose.material)
    implementation(libs.jetpack.compose.toolingPreview)
    debugImplementation(libs.jetpack.compose.tooling)
    debugImplementation(libs.jetpack.compose.testManifest)

    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlin.immutableCollections)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigationCompose)

    implementation(libs.timber)

    lintChecks(libs.lint.composeChecks)

    testImplementation(libs.junit)
    androidTestImplementation(libs.jetpack.test.junit)
    androidTestImplementation(libs.jetpack.espresso)
}
