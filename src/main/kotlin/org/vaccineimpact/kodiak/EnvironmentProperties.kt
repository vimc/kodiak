package org.vaccineimpact.kodiak

object EnvironmentProperties {

    var production = System.getenv("production") != null

    var configSource: String = if (production) {
        "/etc/kodiak/config.json"
    } else {
        EnvironmentProperties::class.java.classLoader
                .getResource("config.json")
                .path
    }
}
