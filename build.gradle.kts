import net.labymod.labygradle.common.extension.model.labymod.ReleaseChannels

plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

group = "org.example"
version = providers.environmentVariable("VERSION").getOrElse("1.0.8-SNAPSHOT")

labyMod {
    defaultPackageName = "de.funboyy.bingo"

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                getByName("client") {
                    // devLogin = true
                }
            }
        }
    }

    addonInfo {
        namespace = "bingo"
        displayName = "Bingo"
        author = "Funboyy"
        description = "This addon improves some points for Bingo on GommeHD"
        minecraftVersion = "1.21<1.21.4"
        version = rootProject.version.toString()
        releaseChannel = ReleaseChannels.SNAPSHOT
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version
}
