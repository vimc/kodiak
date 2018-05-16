package org.vaccineimpact.kodiak

import java.io.File
import java.io.InputStream

object EnvironmentProperties {

    val prod = System.getProperty("production", "false").toBoolean()

    val configSource: String = if (prod) {
        File("/etc/kodiak/config.json").path
    } else {
        EnvironmentProperties::class.java.classLoader
                .getResource("config.json")
                .path
    }
}
