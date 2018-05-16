package org.vaccineimpact.kodiak

import org.slf4j.LoggerFactory

class EnvironmentProperties {

    var configSource: String = "/etc/kodiak/config.json"

    init {
        val dev = System.getProperty("dev", "false").toBoolean()
        LoggerFactory.getLogger(EnvironmentProperties::class.java)
                .info(System.getenv("production"))
        if (dev) {
            configSource = EnvironmentProperties::class.java.classLoader
                    .getResource("config.json")
                    .path
        }
    }
}
