plugins {
    alias(libs.plugins.ngamingcase.kotlin.library)
    alias(libs.plugins.ngamingcase.kotlin.lint)
    alias(libs.plugins.ngamingcase.testing)
}

dependencies {
    implementation(libs.javax.inject)

    testImplementation(libs.truth)
}
