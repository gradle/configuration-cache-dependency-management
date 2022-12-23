plugins {
    id("java-library")
    id("maven-publish")
    id("signing")
}

group = "test"
version = "1.0"
if (System.getProperty("libVersion") != null) {
    version = System.getProperty("libVersion")
}

publishing {
    repositories {
        maven {
            url = uri("build/repo/maven")
        }
    }
    publications {
        create("maven", MavenPublication::class.java) {
            from(components["java"])
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["maven"])
}
