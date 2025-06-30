plugins {
    id("java")
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}
group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin-bundle:6.1.3")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.0")
    implementation("org.json:json:20231013")
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.javalin:javalin-testtools:6.1.3")
    testImplementation("com.squareup.okhttp3:okhttp:4.12.0")
    testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.0")
}

tasks.test {
    useJUnitPlatform()
}