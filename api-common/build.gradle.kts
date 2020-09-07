import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
//	id("org.springframeworkamework.boot") version "2.3.3.RELEASE"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
}

val springBootVersion by extra("2.3.3.RELEASE")

group = "prot.lab.seatademo.common"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.slf4j:slf4j-api:1.7.30")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.2")
	implementation("org.springframework.boot:spring-boot-starter-webflux:${springBootVersion}")
	implementation("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")
	// compileOnly("org.projectlombok:lombok")
	// annotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}
