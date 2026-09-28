import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.ngamingcase.android.library.compose)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.ngamingcase.core.designsystem"
}

dependencies {
    api(projects.core.common)
    api(libs.androidx.lifecycle.viewmodel.compose)
    implementation(projects.core.ui)

    implementation(libs.bundles.compose)
}
