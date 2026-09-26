plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(
        libs.versions.jvmToolchain
            .get()
            .toInt(),
    )
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.hilt.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id =
                libs.plugins.ngamingcase.android.application
                    .get()
                    .pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id =
                libs.plugins.ngamingcase.android.library
                    .asProvider()
                    .get()
                    .pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id =
                libs.plugins.ngamingcase.android.library.compose
                    .get()
                    .pluginId
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("kotlinLibrary") {
            id =
                libs.plugins.ngamingcase.kotlin.library
                    .get()
                    .pluginId
            implementationClass = "KotlinLibraryConventionPlugin"
        }
        register("androidHilt") {
            id =
                libs.plugins.ngamingcase.android.hilt
                    .get()
                    .pluginId
            implementationClass = "HiltConventionPlugin"
        }
        register("androidVersioning") {
            id =
                libs.plugins.ngamingcase.android.versioning
                    .get()
                    .pluginId
            implementationClass = "AndroidVersioningConventionPlugin"
        }
        register("kotlinLint") {
            id =
                libs.plugins.ngamingcase.kotlin.lint
                    .get()
                    .pluginId
            implementationClass = "KotlinLintConventionPlugin"
        }
        register("testing") {
            id =
                libs.plugins.ngamingcase.testing
                    .get()
                    .pluginId
            implementationClass = "TestingConventionPlugin"
        }
    }
}
