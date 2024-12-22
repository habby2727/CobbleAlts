architectury {
    common("forge", "fabric")
    platformSetupLoomIde()
}
plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")

}

dependencies {

    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")

    modCompileOnly("com.cobblemon:mod:${property("cobblemon_version")}")

    modCompileOnly("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")
    modCompileOnly("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")

    modImplementation("dev.architectury:architectury:${property("architectury_version")}")

    modImplementation(files("libs/CobbleUtils-common-1.1.2.jar"))

    // Kyori Adventure
    api("net.kyori:adventure-text-serializer-gson:${property("kyori_version")}")
    api("net.kyori:adventure-text-minimessage:${property("kyori_version")}")

    // Lombok
    annotationProcessor("org.projectlombok:lombok:1.18.20")
    implementation("org.projectlombok:lombok:1.18.20")
}
