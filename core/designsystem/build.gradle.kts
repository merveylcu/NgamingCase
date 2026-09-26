import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.ngamingcase.android.library.compose)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.ngamingcase.core.designsystem"
}

dependencies {
    api(projects.core.common)

    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
}
