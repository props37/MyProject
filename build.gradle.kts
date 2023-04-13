// TODO remove after upgrading to Gradle 8.1
// https://github.com/gradle/gradle/issues/22797
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
