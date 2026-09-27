import com.android.build.api.dsl.ApplicationExtension

plugins {
    alias(libs.plugins.ngamingcase.android.application)
    alias(libs.plugins.ngamingcase.android.hilt)
    alias(libs.plugins.ngamingcase.android.versioning)
}

configure<ApplicationExtension> {
    namespace = "com.merveylcu.ngamingcase"

    defaultConfig {
        applicationId = "com.merveylcu.ngamingcase"
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.database)
    implementation(projects.core.designsystem)
    implementation(projects.navigation)
    implementation(projects.network)
    implementation(projects.feature.posts.data)
    implementation(projects.feature.posts.domain)
    implementation(projects.feature.posts.presentation)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
}
