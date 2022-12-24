plugins {
    id("java-library")
}

/**
 * Comments out a particular artifact in the dependency verification metadata file, breaking verification.
 */
tasks.register("breakVerificationMetadata") {
    val file = rootProject.file("gradle/verification-metadata.xml")
    doLast {
        val startTag = "<artifact name=\"ivy-lib-1.0.jar\">"
        val endTag = "</artifact>"
        val startComment = "<!-- START MARKER"
        val endComment = "END MARKER -->"
        val content = StringBuilder(file.readText())
        if (!content.contains(startComment)) {
            val start = content.indexOf(startTag)
            val end = content.indexOf(endTag, start)
            content.insert(end + endTag.length, endComment)
            content.insert(start, startComment)
            file.writeText(content.toString())
        } else {
            println("Verification file not modified")
        }
    }
}

/**
 * Removes the changes added by `breakVerificationMetadata`
 */
tasks.register("fixVerificationMetadata") {
    val file = rootProject.file("gradle/verification-metadata.xml")
    doLast {
        val startComment = "<!-- START MARKER"
        val endComment = "END MARKER -->"
        val content = StringBuilder(file.readText())
        val start = content.indexOf(startComment)
        val end = content.indexOf(endComment)
        if (start >= 0) {
            content.deleteRange(end, end + endComment.length)
            content.deleteRange(start, start + startComment.length)
            file.writeText(content.toString())
        } else {
            println("Verification file not modified")
        }
    }
}