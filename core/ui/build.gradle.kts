import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.ngamingcase.android.library)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.ngamingcase.core.ui"
}

dependencies {
    api(projects.core.common)

    implementation(libs.androidx.annotation)
}
