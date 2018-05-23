package org.vaccineimpact.kodiak

import org.docopt.Docopt

private val doc = (
        """Usage:
        |  kodiak init [VAULT_TOKEN] [TARGET...]
        |  kodiak backup
        |  kodiak restore""".trimMargin())


fun main(args: Array<String>) {

    val opts = Docopt(doc)
            .parse(args.toList())

    val config = JsonConfig(EnvironmentProperties.configSource)
    val kodiak = Kodiak(config, SodiumEncryption.instance)

    return kodiak.main(opts)
}