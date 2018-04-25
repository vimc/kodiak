package org.vaccineimpact.kodiak

import java.io.File
import java.io.InputStream

object AppConfig {

    private val prod = System.getProperty("prod", "false").toBoolean()

    private val kodiakConfigStream: InputStream = if (prod) {
        File("/etc/kodiak/config.json").inputStream()
    } else {
        AppConfig::class.java.classLoader.getResource("config.json")
                .openStream()
    }

    val kodiakConfig: KodiakConfig = JsonConfig(kodiakConfigStream)
}
