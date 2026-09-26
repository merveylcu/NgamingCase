import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class KotlinLintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Treat all Kotlin compiler warnings as errors for strict JVM library modules.
            // Detekt static analysis is already provided by KotlinLibraryConventionPlugin.
            // Applying this to domain modules enforces zero-warning policy at compile time.
            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    allWarningsAsErrors.set(true)
                }
            }
        }
    }
}
