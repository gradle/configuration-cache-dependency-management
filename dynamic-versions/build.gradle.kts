plugins {
    id("java-library")
}

repositories {
    maven {
        url = uri("../lib1/build/repo/maven")
    }
    ivy {
        url = uri("../lib1/build/repo/ivy")
    }
}

dependencies {
    implementation("test:lib1-maven:+")
    implementation("test:lib1-ivy:+")
}

tasks.register("resolve") {
    val files: FileCollection = configurations.compileClasspath.get()
    doLast {
        println("files=${files.files}")
    }
}