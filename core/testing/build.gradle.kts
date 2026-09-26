plugins {
    alias(libs.plugins.ngamingcase.kotlin.library)
    alias(libs.plugins.ngamingcase.kotlin.lint)
}

dependencies {
    implementation(libs.junit)
    implementation(libs.kotlinx.coroutines.test)
}
