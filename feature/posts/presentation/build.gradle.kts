import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.ngamingcase.android.library.compose)
    alias(libs.plugins.ngamingcase.android.hilt)
    alias(libs.plugins.ngamingcase.testing)
    alias(libs.plugins.kotlin.serialization)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.ngamingcase.feature.posts.presentation"
}

dependencies {
    implementation(projects.feature.posts.domain)
    implementation(projects.core.designsystem)
    implementation(projects.navigation)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.coil)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serialization.core)

    testImplementation(projects.core.testing)
    testImplementation(libs.truth)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.runner)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
