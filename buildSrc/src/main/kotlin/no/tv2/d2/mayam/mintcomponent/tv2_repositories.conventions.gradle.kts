
val artifactory_user: String by project
val artifactory_password: String by project
val artifactoryBaseUrl: String by project

repositories {
    mavenLocal()
    mavenCentral()
}