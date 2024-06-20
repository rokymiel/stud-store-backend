plugins {
    id("java")
    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "ru.hse.store"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security") // caution
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("com.github.java-json-tools:json-patch:1.13")
    implementation("org.springframework:spring-test:6.0.6")
    implementation("org.telegram:telegrambots:6.1.0")
    implementation("org.telegram:telegrambotsextensions:6.1.0")
    implementation("org.telegram:telegrambots-spring-boot-starter:6.1.0")
    implementation("junit:junit:4.13.1")
    compileOnly("org.projectlombok:lombok:1.18.24")
//    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.0.1")
    testImplementation("org.springframework.security:spring-security-test:5.7.3") // caution
    implementation("com.github.pengrad:java-telegram-bot-api:6.5.0")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation("org.postgresql:postgresql:42.2.21")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    // https://stackoverflow.com/questions/42966880/java-lang-noclassdeffounderror-javax-xml-bind-datatypeconverter
    implementation("javax.xml.bind:jaxb-api:2.3.0")
}


tasks.named("compileJava") {
    inputs.files(tasks.named("processResources"))
}

tasks.named("test") {
//    useJUnitPlatform()
}
