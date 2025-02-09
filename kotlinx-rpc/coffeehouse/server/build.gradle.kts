plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)

    application
}

dependencies {
    implementation(projects.shared)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.cors.jvm)
    implementation(libs.ktor.server.websockets.jvm)
    implementation(libs.ktor.server.host.common.jvm)
    implementation(libs.kotlinx.rpc.krpc.ktor.server)
    implementation(libs.logback)

    testImplementation(libs.kotlin.test.junit)
}

application {
    mainClass.set("coffeehouse.server.ServerApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}
