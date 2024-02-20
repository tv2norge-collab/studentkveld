plugins {
    id(Conventions.tv2Repos)
    id(Conventions.jupiter)
    id(Conventions.kotlin)
    id("io.kotest") version "0.3.9"
}

dependencies {
    implementation(libs.kafkaStreams)
    implementation(libs.bundles.jackson)

    testImplementation(libs.bundles.unitTest)
}
