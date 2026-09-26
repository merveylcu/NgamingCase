plugins {
    alias(libs.plugins.ngamingcase.kotlin.library)
    alias(libs.plugins.ngamingcase.kotlin.lint)
    alias(libs.plugins.ngamingcase.testing)
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
