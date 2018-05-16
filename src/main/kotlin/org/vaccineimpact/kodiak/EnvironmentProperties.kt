package org.vaccineimpact.kodiak

class EnvironmentProperties {

    val production = System.getProperty("production", "false").toBoolean()

    val configSource: String = if (production) {
        "/etc/kodiak/config.json"
    } else {
        EnvironmentProperties::class.java.classLoader
                .getResource("config.json")
                .path
    }
}
