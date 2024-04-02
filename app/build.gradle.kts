import com.android.build.api.dsl.VariantDimension
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt)
    alias(libs.plugins.googlePlayServices)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.performance)

    alias(libs.plugins.androidGitVersion)
    alias(libs.plugins.checkDependencyUpdates)
}

androidGitVersion {
    codeFormat = "MNNPPP"
    format = "%tag%%-branch%%-commit%"
}

kapt {
    correctErrorTypes = true
}

android {
    val appId = "ru.zarina.zarina"

    namespace = appId
    compileSdk = 34

    val generatedVersionCode = androidGitVersion.code()
    val generatedVersionName = androidGitVersion.name()
    println("Version code: $generatedVersionCode")
    println("Version name: $generatedVersionName")

    defaultConfig {
        applicationId = appId
        minSdk = 24
        targetSdk = 34
        versionCode = generatedVersionCode
        versionName = generatedVersionName

        resourceConfigurations.add("ru")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        ZarinaSigningVariant.values().forEach { variant ->
            maybeCreate(variant.name).apply {
                val signingDir = File(rootDir, "/signing/${variant.name.lowercase()}")
                val signingPropertiesFile = File(signingDir, "signing.properties")
                val properties = Properties().apply { load(FileInputStream(signingPropertiesFile)) }
                storeFile = properties.getProperty("storeFile")?.let { File(project.rootDir, it) }
                storePassword = properties.getProperty("storePassword")
                keyAlias = properties.getProperty("keyAlias")
                keyPassword = properties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        all {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }

        ZarinaBuildType.all.forEach { buildType ->
            maybeCreate(buildType.name).apply {
                isDebuggable = buildType.isDebuggable
                isMinifyEnabled = buildType.isMinifyEnabled
                isShrinkResources = buildType.isShrinkResources

                applicationIdSuffix = buildType.applicationIdSuffix
                versionNameSuffix = buildType.versionNameSuffix

                signingConfig = signingConfigs.getByName(buildType.signingVariant.name)

                resStringValue(Keys.APP_NAME, buildType.applicationName)
                buildConfigBooleanField(Keys.IS_LOGGING_ENABLED, buildType.isLoggingEnabled)
                buildConfigStringField(Keys.BACKEND_URL, buildType.backendUrl)
                buildConfigStringField(Keys.MINDBOX_ENDPOINT, buildType.mindboxEndpoint)
                buildConfigStringField(Keys.MINDBOX_KEY, buildType.mindboxKey)
                buildConfigStringField(Keys.RECAPTCHA_KEY, buildType.recaptchaKey)
                buildConfigStringField(Keys.ANY_QUERY_KEY, buildType.anyQueryKey)
                manifestPlaceholders[Keys.GOOGLE_MAPS_KEY] = buildType.googleMapsKey
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.jetpack.compose.compiler.get()
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/room_schemas")
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// Compose compiler setup
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    // Compiler metrics
    // ./gradlew assembleRelease -P.enableComposeCompilerReports=true --rerun-tasks
    val buildDir = project.layout.buildDirectory.asFile.get()
    val metricsDir = "${buildDir.absolutePath}/compose_metrics"
    kotlinOptions.freeCompilerArgs += listOf(
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$metricsDir",
    )
    kotlinOptions.freeCompilerArgs += listOf(
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$metricsDir",
    )

    // Compiler stability config
    val stabilityConfigPath = "${project.rootDir.absolutePath}/config/compose/stability_config.txt"
    kotlinOptions.freeCompilerArgs += listOf(
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=$stabilityConfigPath",
    )
}

dependencies {
    implementation(libs.jetpack.core)
    implementation(libs.jetpack.activity.compose)
    implementation(libs.jetpack.lifecycle.runtime.compose)
    implementation(libs.jetpack.lifecycle.viewModel.compose)
    implementation(libs.jetpack.navigation.compose)
    implementation(libs.jetpack.dataStore.preferences)
    implementation(libs.jetpack.room)
    ksp(libs.jetpack.room.compiler)
    implementation(libs.jetpack.splashScreen)
    implementation(libs.jetpack.media3.exoplayer)
    implementation(libs.jetpack.media3.ui)
    implementation(libs.jetpack.paging.compose)
    implementation(libs.jetpack.security.cripto)
    implementation(libs.jetpack.browser)

    implementation(platform(libs.jetpack.compose.bom))
    implementation(libs.jetpack.compose.ui)
    implementation(libs.jetpack.compose.material)
    implementation(libs.jetpack.compose.material3)
    implementation(libs.jetpack.compose.toolingPreview)
    debugImplementation(libs.jetpack.compose.tooling)
    debugImplementation(libs.jetpack.compose.testManifest)

    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlin.immutableCollections)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.json)

    implementation(libs.hilt)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigationCompose)

    implementation(libs.koin.annotations)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    ksp(libs.koin.compiler)

    implementation(libs.accompanist.systemUi)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.navigationMaterial)
    implementation(libs.accompanist.webview)

    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.composeShimmer)

    implementation(libs.libphonenumber)

    implementation(libs.timber)

    implementation(libs.googlePlayServices.location)
    implementation(libs.googlePlayServices.maps)
    implementation(libs.googlePlayServices.maps.compose)

    implementation(libs.recaptcha)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.performance)

    implementation(libs.mindbox)

    debugImplementation(libs.leakCanary)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.koin.test)
    androidTestImplementation(libs.jetpack.test.junit)
    androidTestImplementation(libs.jetpack.espresso)
    androidTestImplementation(platform(libs.jetpack.compose.bom))
    androidTestImplementation(libs.jetpack.compose.junit4)

    lintChecks(libs.lint.composeChecks)
}

fun VariantDimension.buildConfigStringField(name: String, value: String) {
    buildConfigField("String", name, "\"$value\"")
}

fun VariantDimension.buildConfigBooleanField(name: String, value: Boolean) {
    buildConfigField("boolean", name, "$value")
}

fun VariantDimension.resStringValue(name: String, value: String) {
    resValue("string", name, value)
}
