import com.android.build.api.dsl.VariantDimension
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.compose.compiler)
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

android {
    val appId = "ru.livetyping.zarina"

    namespace = appId
    compileSdk = 35

    val generatedVersionCode = androidGitVersion.code()
    val generatedVersionName = androidGitVersion.name()
    println("Version code: $generatedVersionCode")
    println("Version name: $generatedVersionName")

    defaultConfig {
        applicationId = appId
        minSdk = 24
        targetSdk = 35
        versionCode = generatedVersionCode
        versionName = generatedVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    androidResources {
        generateLocaleConfig = true
    }

    lint {
        disable += listOf("UsingMaterialAndMaterial3Libraries", "ComposeUnstableCollections")
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
                buildConfigStringField(Keys.ANY_QUERY_KEY, buildType.anyQueryKey)
                assetLink(buildType.backendUrl)
                manifestPlaceholders[Keys.GOOGLE_MAPS_KEY] = buildType.googleMapsKey

                matchingFallbacks += buildType.matchingFallbacks
            }
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
        compose = true
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

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    stabilityConfigurationFiles.add(rootProject.layout.projectDirectory.file("config/compose/stability_config.txt"))
}

// TODO: [Top] Optimize dependencies
dependencies {
    implementation(projects.feature.home.ui.api)
    implementation(projects.feature.home.ui.impl)
    implementation(projects.feature.home.domain)
    implementation(projects.feature.home.data)

    implementation(projects.feature.catalog.ui.api)
    implementation(projects.feature.catalog.ui.impl)

    implementation(projects.feature.wishlist.ui.api)
    implementation(projects.feature.wishlist.ui.impl)

    implementation(projects.feature.profile.ui.api)
    implementation(projects.feature.profile.ui.impl)

    implementation(projects.feature.cart.ui.api)
    implementation(projects.feature.cart.ui.impl)

    implementation(projects.feature.onboarding.ui.api)
    implementation(projects.feature.onboarding.ui.impl)

    implementation(projects.feature.citySelector.ui.api)
    implementation(projects.feature.citySelector.ui.impl)

    implementation(projects.feature.signin.ui.api)
    implementation(projects.feature.signin.ui.impl)

    implementation(projects.feature.signup.ui.api)
    implementation(projects.feature.signup.ui.impl)

    implementation(projects.feature.productList.ui.api)
    implementation(projects.feature.productList.ui.impl)

    implementation(projects.feature.product.ui.api)
    implementation(projects.feature.product.ui.impl)

    implementation(projects.feature.productSubscription.ui.api)
    implementation(projects.feature.productSubscription.ui.impl)

    implementation(projects.data.content)
    implementation(projects.data.auth)
    implementation(projects.data.category)
    implementation(projects.data.location)
    implementation(projects.data.onboarding)
    implementation(projects.data.user)
    implementation(projects.data.wishlist)
    implementation(projects.data.geography)
    implementation(projects.data.order)
    implementation(projects.data.product)
    implementation(projects.data.cart)

    implementation(projects.core.buildUtil)
    implementation(projects.core.network)
    implementation(projects.core.database)
    implementation(projects.core.uiCompose)
    implementation(projects.core.uiKit)
    implementation(projects.core.sharedpreferences)
    implementation(projects.core.permission)
    implementation(projects.core.navigationUtil)
    implementation(projects.core.credential)
    implementation(projects.core.googlePlayServices)

    implementation(libs.jetpack.core)
    implementation(libs.jetpack.appcompat)
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
    implementation(libs.jetpack.security.crypto)
    implementation(libs.jetpack.browser)
    implementation(libs.jetpack.credentials)
    implementation(libs.jetpack.credentials.compat)
    implementation(libs.jetpack.webkit)

    implementation(platform(libs.jetpack.compose.bom))
    implementation(libs.jetpack.compose.ui)
    implementation(libs.jetpack.compose.material)
    implementation(libs.jetpack.compose.material.navigation)
    implementation(libs.jetpack.compose.material3)
    implementation(libs.jetpack.compose.toolingPreview)
    debugImplementation(libs.jetpack.compose.tooling)
    debugImplementation(libs.jetpack.compose.testManifest)

    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlin.immutableCollections)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.json)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigationCompose)

    implementation(libs.accompanist.permissions)

    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.composeShimmer)
    implementation(libs.qrcode)

    implementation(libs.libphonenumber)

    implementation(libs.timber)

    implementation(libs.googlePlayServices.location)
    implementation(libs.googlePlayServices.maps)
    implementation(libs.googlePlayServices.maps.compose)
    implementation(libs.googlePlayServices.maps.compose.utils)
    implementation(libs.googlePlayServices.auth.api.phone)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.performance)

    implementation(libs.mindbox)
    implementation(libs.mindbox.firebase)

    debugImplementation(libs.leakCanary)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.coroutines.test)
    androidTestImplementation(libs.jetpack.test.junit)
    androidTestImplementation(libs.jetpack.espresso)
    androidTestImplementation(platform(libs.jetpack.compose.bom))
    androidTestImplementation(libs.jetpack.compose.junit4)

    lintChecks(libs.lint.composeChecks)

    coreLibraryDesugaring(libs.coreLibraryDesugaring)
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

fun VariantDimension.assetLink(backendUrl: String) {
    resStringValue(
        name = Keys.ASSET_LINK,
        value = "\n[{\n" +
                "  \\\"include\\\": \\\"$backendUrl/.well-known/assetlinks.json\\\"\n" +
                "}]\n",
    )
}
