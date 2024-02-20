val artifactory_user: String by project
val artifactory_password: String by project
val artifactoryBaseUrl: String by project

group = "no.tv2.di.pip"

plugins {
    `maven-publish`
    `java`
}

publishing {
    repositories {
        maven {
            val releasesRepoUrl = "${artifactoryBaseUrl}/libs-releases-local"
            val snapshotsRepoUrl = "${artifactoryBaseUrl}/libs-snapshots-local"
            url = uri(if (version.toString().contains("-")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                username = artifactory_user
                password = artifactory_password
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            val sourcesJar by tasks.creating(Jar::class) {
                val sourceSets: SourceSetContainer by project
                from(sourceSets["main"].allSource)
                archiveClassifier.set("sources")
            }
            artifact(sourcesJar)
        }
    }
}
