import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.ngamingcase.android.library)
    alias(libs.plugins.ngamingcase.android.hilt)
    alias(libs.plugins.ngamingcase.testing)
    alias(libs.plugins.kotlin.serialization)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.ngamingcase.network"
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://jsonplaceholder.typicode.com/\"")
    }
}

dependencies {
    implementation(projects.core.common)

    implementation(libs.bundles.network)
    implementation(libs.bundles.coroutines)

    testImplementation(libs.truth)
}
