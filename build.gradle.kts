plugins {
    id("org.jetbrains.intellij") version "0.4.10"
    java
}

group = "org.jetbrains.intellij"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testCompile("junit", "junit", "4.12")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "193-EAP-SNAPSHOT"

}
configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    pluginDescription("""
        Enables experimental features that deal with various focus issues, for example, when the IDE steals the focus from another application, and other unexpected behavior.
        <br/>
        <br/>
        These features are considered experimental because they may lead to other unexpected problems. Install this plugin only if advised to do so by JetBrains support, and let us know if anything goes wrong.
      """)
}