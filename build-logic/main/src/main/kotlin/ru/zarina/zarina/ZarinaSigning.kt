package ru.zarina.zarina

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.util.Properties

enum class ZarinaSigningVariant { INTERNAL, RELEASE }

fun Project.configureSigning(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply commonExtension@{
        signingConfigs {
            ZarinaSigningVariant.values().forEach { zarinaVariant ->
                create(zarinaVariant.name) {
                    val file = project.getSigningFile(zarinaVariant, "signing.properties")
                    val properties = Properties().apply { load(FileInputStream(file)) }
                    storeFile =
                        properties.getProperty("storeFile")?.let { File(project.rootDir, it) }
                    storePassword = properties.getProperty("storePassword")
                    keyAlias = properties.getProperty("keyAlias")
                    keyPassword = properties.getProperty("keyPassword")
                }
            }
        }
    }
}

private fun Project.getSigningFile(variant: ZarinaSigningVariant, path: String): File {
    return File(File(this.rootDir, "/signing/${variant.name.lowercase()}"), path)
}