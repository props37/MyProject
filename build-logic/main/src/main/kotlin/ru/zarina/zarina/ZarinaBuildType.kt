package ru.zarina.zarina

import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

sealed interface ZarinaBuildType {

    val name: String

    val applicationName: String
        get() = "$BASE_NAME $name"

    val applicationIdSuffix: String?
        get() = ".${name}"

    val versionNameSuffix: String?
        get() = "-$name"

    val isDebuggable: Boolean
        get() = false

    val isMinifyEnabled: Boolean
        get() = true

    val isShrinkResources: Boolean
        get() = true

    val initializeWith: ZarinaBuildType?
        get() = null

    val matchingFallbacks: List<ZarinaBuildType>
        get() = emptyList()

    val backendUrl: String
        get() = "https://zarina.ru"

    object Debug : ZarinaBuildType {
        override val name = "debug"
        override val isDebuggable = true
        override val isMinifyEnabled = false
        override val isShrinkResources = false
        override val backendUrl = "https://test.zarina.ru"
    }

    object Qa : ZarinaBuildType {
        override val name = "qa"
        override val backendUrl = "https://test.zarina.ru"
    }

    object Release : ZarinaBuildType {
        override val name = "release"
        override val applicationName = BASE_NAME
        override val applicationIdSuffix = null
        override val versionNameSuffix = null
    }

    object _Benchmark : ZarinaBuildType {
        override val name = "benchmark"
        override val initializeWith = Release
        override val matchingFallbacks = listOf(Release)
        override val backendUrl = "https://test.zarina.ru"
    }

    companion object {
        val values = listOf(Debug, Qa, Release, _Benchmark)

        private const val BASE_NAME = "Zarina"
    }
}

@Suppress("UnusedReceiverParameter")
fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *>,
    buildTypeConfigurationBlock: BuildType.(ZarinaBuildType) -> Unit = {},
) {
    commonExtension.apply commonExtension@{
        buildTypes {
            ZarinaBuildType.values.forEach { buildType ->
                maybeCreate(buildType.name).apply {
                    if (this@commonExtension is ApplicationExtension && this is ApplicationBuildType) {
                        buildType.initializeWith?.let { initWith(getByName(it.name)) }
                        if (buildType.matchingFallbacks.isNotEmpty())
                            matchingFallbacks += buildType.matchingFallbacks.map { it.name }

                        resValue("string", "app_name", buildType.applicationName)
                        applicationIdSuffix = buildType.applicationIdSuffix
                        isDebuggable = buildType.isDebuggable
                        isMinifyEnabled = buildType.isMinifyEnabled
                        isShrinkResources = buildType.isShrinkResources
                        versionNameSuffix = buildType.versionNameSuffix
                        fillBuildConfigFields(buildType)
                    }
                    buildTypeConfigurationBlock(this, buildType)
                }
            }
        }
    }
}

private fun ApplicationBuildType.fillBuildConfigFields(buildType: ZarinaBuildType) {
    buildConfigStringField("BACKEND_URL", buildType.backendUrl)
}

private fun ApplicationBuildType.buildConfigStringField(
    name: String,
    value: String,
) {
    buildConfigField("String", name, "\"$value\"")
}

