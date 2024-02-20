plugins {
    kotlin("jvm")
}

tasks {
    test {
        useJUnitPlatform()
        maxParallelForks = 2
    }
}