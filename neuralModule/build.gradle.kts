
repositories {
    mavenCentral()
    google()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    api(group = "org.deeplearning4j", name = "deeplearning4j-core", version = "1.0.0-M2.1")
    api(group = "org.nd4j", name = "nd4j-native-platform", version = "1.0.0-M2.1")
    api("org.deeplearning4j:deeplearning4j-datasets:1.0.0-M2.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
