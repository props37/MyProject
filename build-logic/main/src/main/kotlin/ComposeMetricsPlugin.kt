import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class ComposeMetricsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks
            .withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java)
            .configureEach {
                kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"

                // Compose metrics
                // Command: ./gradlew assembleRelease -P.enableComposeCompilerReports=true --rerun-tasks
                kotlinOptions.freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                            "${project.buildDir.absolutePath}/composeMetrics"
                )
                kotlinOptions.freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                            "${project.buildDir.absolutePath}/composeMetrics"
                )
            }
    }
}
