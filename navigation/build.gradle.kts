import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.ngamingcase.android.library.compose)
    alias(libs.plugins.ngamingcase.testing)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.ngamingcase.navigation"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    api(libs.navigation3.runtime)
    implementation(libs.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    testImplementation(libs.truth)
}
