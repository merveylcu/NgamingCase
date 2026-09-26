plugins {
    alias(libs.plugins.ngamingcase.kotlin.library)
    alias(libs.plugins.ngamingcase.kotlin.lint)
}

dependencies {
    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
}
