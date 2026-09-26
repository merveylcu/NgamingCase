import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin that adds standard unit-test dependencies to any module.
 *
 * Apply with: alias(libs.plugins.ngamingcase.testing)
 *
 * Provides:
 *   - JUnit 4              — test runner
 *   - kotlinx-coroutines-test — CoroutineDispatcher test utilities, runTest
 *   - Turbine              — Flow assertion library (app.cash.turbine)
 *   - MockK                — Kotlin-idiomatic mocking library
 *
 * For Android instrumented tests, add androidTestImplementation deps directly
 * in the module's build.gradle.kts (Hilt testing, Espresso, Compose UI tests).
 */
class TestingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions
                .getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java)
                .named("libs")

            dependencies {
                "testImplementation"(libs.findLibrary("junit").get())
                "testImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
                "testImplementation"(libs.findLibrary("turbine").get())
                "testImplementation"(libs.findLibrary("mockk").get())
            }
        }
    }
}
