package org.vaccineimpact.kodiak

import java.io.File
import java.io.InputStream

object EnvironmentProperties {

    val prod = System.getProperty("prod", "false").toBoolean()

    val configSource: InputStream = if (prod) {
        File("/etc/kodiak/config.json").inputStream()
    } else {
        EnvironmentProperties::class.java.classLoader.getResource("config.json")
                .openStream()
    }
}
