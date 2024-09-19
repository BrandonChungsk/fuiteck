// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
}

// Add the task to be recognized by Heroku
tasks.register("stage") {
    dependsOn("clean", "build")
    doLast {
        // Copy the JAR file to a location that Heroku recognizes
        copy {
            from(layout.buildDirectory.dir("libs"))
            into(layout.buildDirectory.dir("staged"))
            include("*.jar")
            rename { "app.jar" }
        }
    }
}