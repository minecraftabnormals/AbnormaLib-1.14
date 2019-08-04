import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapSourcesJarTask

plugins {
	wrapper
	idea
	id("fabric-loom") version Fabric.Loom.version
	id("maven-publish")
	kotlin("jvm") version "1.3.21"
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

idea {
	module {
		excludeDirs.add(file("run"))
	}
}

base {
	archivesBaseName = Constants.name
}

version = "${Constants.version}+${Constants.minecraftVersionVer}"
group = Constants.group

repositories {
	mavenCentral()
	mavenLocal()
	maven("https://maven.fabricmc.net")
	maven("https://minecraft.curseforge.com/api/maven")
	maven("https://maven.jamieswhiteshirt.com/libs-release/")
	maven("http://server.bbkr.space:8081/artifactory/libs-snapshot")
	maven("http://maven.sargunv.s3-website-us-west-2.amazonaws.com/")
}

dependencies {
	minecraft(group = "com.mojang", name = "minecraft", version = Minecraft.version)
	mappings(group = "net.fabricmc", name = "yarn", version = "${Minecraft.version}+build.${Fabric.Yarn.version}")

	modApi(group = "net.fabricmc", name = "fabric-loader", version = Fabric.Loader.version)

	modApi(group = "net.fabricmc.fabric-api", name = "fabric-api", version = Fabric.API.version)

	modApi(group = "io.github.prospector", name = "modmenu", version = Dependencies.ModMenu.version)
}

val javaCompile = tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

val sourcesJar by tasks.registering(Jar::class) {
	from(sourceSets["main"].allSource)
	classifier = "sources"
}

val jar = tasks.getByName<Jar>("jar") {
    from("LICENSE")
}

val remapSourcesJar: RemapSourcesJarTask by tasks.getting {}

val remapJar: RemapJarTask by tasks.getting {}

publishing {
	publications {
		afterEvaluate {
			register("mavenLocal", MavenPublication::class) {
				artifactId = Constants.name
				artifact("${project.buildDir.absolutePath}/libs/${base.archivesBaseName}-${project.version}.jar") { //release jar - file location not provided anywhere in loom
					classifier = null
					builtBy(remapJar)
				}
				artifact ("${project.buildDir.absolutePath}/libs/${base.archivesBaseName}-${project.version}-dev.jar") { //release jar - file location not provided anywhere in loom
					classifier = "dev"
					builtBy(remapJar)
				}
				artifact ("${project.buildDir.absolutePath}/libs/${base.archivesBaseName}-${project.version}-sources.jar") { //release jar - file location not provided anywhere in loom
					classifier = "sources"
					builtBy(remapJar)
				}
				artifact ("${project.buildDir.absolutePath}/libs/${base.archivesBaseName}-${project.version}-sources-dev.jar") { //release jar - file location not provided anywhere in loom
					classifier = "sources-dev"
					builtBy(remapJar)
				}
				pom {
					name.set(Constants.name)
					description.set(Constants.description)
					url.set(Constants.url)
					licenses {
						license {
							name.set("MIT License")
							url.set("https://tldrlegal.com/license/mit-license#fulltext")
						}
					}
					developers {
						developer {
							id.set("vampire-studios")
							name.set("Vampire Studios")
						}
					}
				}
			}
		}
	}
}