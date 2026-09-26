import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class KotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.jvm")
            pluginManager.apply("java-library")
            pluginManager.apply("com.android.lint")

            extensions.configure<KotlinJvmProjectExtension> {
                // JVM_TOOLCHAIN: which JDK version runs the compiler.
                // jvmTarget: which JVM bytecode version is produced (Android-compatible).
                jvmToolchain(ProjectConfig.JVM_TOOLCHAIN)
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }

            // Align Java bytecode target with Kotlin's jvmTarget to avoid
            // "Inconsistent JVM-target compatibility" Gradle validation error.
            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }

            configureDetekt()
            configureJvmLint()
            configureKotlinExplicitApi()
        }
    }
}
