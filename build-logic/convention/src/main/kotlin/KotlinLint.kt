import com.android.build.api.dsl.Lint
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureJvmLint() {
    extensions.configure<Lint>("lint") {
        abortOnError = true
        warningsAsErrors = true
        checkDependencies = true
        lintConfig = rootProject.file("config/lint/lint.xml")
        val baselineFile = project.file("lint-baseline.xml")
        if (baselineFile.exists()) {
            baseline = baselineFile
        }
    }
}
