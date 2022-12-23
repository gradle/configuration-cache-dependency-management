plugins {
    id("java-library")
    id("maven-publish")
    id("ivy-publish")
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
        ivy {
            url = uri("build/repo/ivy")
        }
    }
    publications {
        create("maven", MavenPublication::class.java) {
            from(components["java"])
            artifactId = "lib1-maven"
        }
        create("ivy", IvyPublication::class.java) {
            from(components["java"])
            module = "lib1-ivy"
        }
    }
}
