plugins {
    id("java-library")
    id("ivy-publish")
    id("signing")
}

group = "test"
version = "1.0"
if (System.getProperty("libVersion") != null) {
    version = System.getProperty("libVersion")
}

publishing {
    repositories {
        ivy {
            url = uri("build/repo/ivy")
        }
    }
    publications {
        create("ivy", IvyPublication::class.java) {
            from(components["java"])
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["ivy"])
}
