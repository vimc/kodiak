package org.vaccineimpact.kodiak

import java.io.File
import java.io.InputStream
import java.util.*

object DefaultConfig : AppConfig {

    private val properties = Properties().apply {
        load(AppConfig::class.java.classLoader.getResource("config.properties")
                .openStream())
    }

    private operator fun get(key: String): String {
        val x = properties[key]
        if (x != null) {
            return x as String
        } else {
            throw MissingConfigurationKey(key)
        }
    }

    val prod = System.getProperty("prod", "false").toBoolean()

    override val kodiakConfigStream: InputStream = if (prod) {
        File("/etc/kodiak/config.json").inputStream()
    } else {
        DefaultConfig::class.java.classLoader.getResource("config.json")
                .openStream()
    }

}

interface AppConfig {
    val kodiakConfigStream: InputStream
}
