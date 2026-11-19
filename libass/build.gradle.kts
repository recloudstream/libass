plugins {
    id("maven-publish")
    id("android-library")
}
android {
    compileSdk = 37
    namespace = "com.lagradost.libass"

    defaultConfig {
        minSdk = 21
        version = "1.0.0"

        externalNativeBuild {
            cmake {
                // Static C++ runtime so it can not clash with the other native libraries
                arguments += "-DANDROID_STL=c++_static"
                // Shared between ABIs and variants so the sources are only downloaded once
                arguments += "-DLIBASS_DEPS_DIR=${layout.buildDirectory.get().asFile.invariantSeparatorsPath}/libass-deps"
            }
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    // libass for ASS/SSA subtitles
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
    publications {
        create<MavenPublication>("jitpack") {
            groupId = "com.github.recloudstream"
            artifactId = "libass"
            version = "1.0.0"
            afterEvaluate {
                from(components["release"])
            }
        }
        create<MavenPublication>("maven") {
            groupId = "com.lagradost"
            artifactId = "libass"
            version = "1.0.0"
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}
