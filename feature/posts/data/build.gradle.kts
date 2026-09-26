import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.ngamingcase.android.library)
    alias(libs.plugins.ngamingcase.android.hilt)
    alias(libs.plugins.ngamingcase.testing)
    alias(libs.plugins.kotlin.serialization)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.ngamingcase.feature.posts.data"
}

dependencies {
    implementation(projects.feature.posts.domain)
    implementation(projects.core.common)
    implementation(projects.core.database)
    implementation(projects.network)

    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.coroutines)

    testImplementation(projects.core.testing)
    testImplementation(libs.truth)
}
