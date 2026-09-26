import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.ngamingcase.android.library.compose)
    alias(libs.plugins.ngamingcase.android.hilt)
    alias(libs.plugins.ngamingcase.testing)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.ngamingcase.feature.posts.presentation"
}

dependencies {
    implementation(projects.feature.posts.domain)
    implementation(projects.core.designsystem)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.coil)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.collections.immutable)

    testImplementation(projects.core.testing)
    testImplementation(libs.truth)
}
