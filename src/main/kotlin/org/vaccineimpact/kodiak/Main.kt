package org.vaccineimpact.kodiak

import org.docopt.Docopt

fun main(args: Array<String>) {

    val opts = Docopt(doc)
            .parse(args.toList())

    val config = JsonConfig(EnvironmentProperties.configSource)
    val kodiak = Kodiak(config, SodiumEncryption.instance)

    return kodiak.run(opts)
}