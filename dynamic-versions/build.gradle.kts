plugins {
    id("java-library")
}

repositories {
    maven {
        url = uri("../maven-lib/build/repo/maven")
    }
    ivy {
        url = uri("../ivy-lib/build/repo/ivy")
    }
}

dependencies {
    implementation("test:maven-lib:+")
    implementation("test:ivy-lib:+")
}

tasks.register("resolve") {
    val files: FileCollection = configurations.compileClasspath.get()
    doLast {
        for (file in files) {
            println(file.name)
        }
    }
}