import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    id("io.papermc.paperweight.patcher")
}

paperweight {
    upstreams.create("purpur") {
        repo.set(github("purpurmc", "purpur"))
        ref = providers.gradleProperty("purpurRef")

        // Setup file patches for build scripts
        patchFile {
            path = "purpur-server/build.gradle.kts"
            outputFile = file("aspaper-server/build.gradle.kts")
            patchFile = file("aspaper-server/build.gradle.kts.patch")
        }
        patchFile {
            path = "purpur-api/build.gradle.kts"
            outputFile = file("aspaper-api/build.gradle.kts")
            patchFile = file("aspaper-api/build.gradle.kts.patch")
        }

        patchRepo("paperApi") {
            upstreamPath = "paper-api"
            patchesDir = file("aspaper-api/paper-patches")
            outputDir = file("paper-api")
        }
        patchDir("purpurApi") {
            upstreamPath = "purpur-api"
            excludes = listOf("build.gradle.kts", "build.gradle.kts.patch", "paper-patches")
            patchesDir = file("aspaper-api/purpur-patches")
            outputDir = file("purpur-api")
        }
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(JAVA_VERSION)
        }
    }

    repositories {
        mavenCentral()
        maven(PAPER_MAVEN_PUBLIC_URL)
    }

    tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = JAVA_VERSION
        options.isFork = true
    }
    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }
    tasks.withType<Test> {
        testLogging {
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
            events(TestLogEvent.STANDARD_OUT)
        }
    }
}
