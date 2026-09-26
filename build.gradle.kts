plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.module.graph.assert)
    alias(libs.plugins.spotless)
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**/*.kt")
        ktlint(libs.versions.ktlint.get())
            .editorConfigOverride(
                mapOf(
                    "ktlint_standard_filename" to "disabled",
                    "ktlint_standard_function-naming" to "disabled",
                    "ij_kotlin_allow_trailing_comma" to "true",
                    "ij_kotlin_allow_trailing_comma_on_call_site" to "true",
                    // Keep in sync with detekt MaxLineLength.
                    "max_line_length" to "140",
                    "ktlint_function_signature_rule_force_multiline_when_parameter_count_greater_or_equal_than" to "3",
                    "ktlint_class_signature_rule_force_multiline_when_parameter_count_greater_or_equal_than" to "3",
                ),
            )
    }
    kotlinGradle {
        target("**/*.kts")
        targetExclude("**/build/**/*.kts")
        ktlint(libs.versions.ktlint.get())
    }
}

moduleGraphAssert {
    maxHeight = 4
    restricted =
        arrayOf(
            ":core:.* -X> :feature:.*",
            ":navigation -X> :feature:.*",
            ":feature:.*:domain -X> :feature:.*:data",
            ":feature:.*:domain -X> :feature:.*:presentation",
            ":feature:.*:domain -X> :network",
            ":feature:.*:presentation -X> :network",
            ":feature:.*:presentation -X> :feature:.*:data",
            ":core:.* -X> :network",
            ":navigation -X> :network",
        )
}
