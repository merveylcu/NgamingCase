import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.ngamingcase.android.library.compose)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.ngamingcase.core.designsystem"
}

dependencies {
    implementation(libs.bundles.compose)
}
