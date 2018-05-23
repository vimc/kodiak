package org.vaccineimpact.kodiak

import org.docopt.Docopt
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val doc = (
        """Usage:
        |  kodiak init [TARGET...]
        |  kodiak backup
        |  kodiak restore""".trimMargin())


fun main(args: Array<String>) {

    val opts = Docopt(doc)
            .parse(args.toList())

    val config = JsonConfig(EnvironmentProperties.configSource)
    val kodiak = Kodiak(config)

    return kodiak.main(opts)
}

class Kodiak(private val config: Config,
             private val logger: Logger = LoggerFactory.getLogger(Kodiak::class.java)) {

    fun main(opts: Map<String, Any>) {

        if (opts["init"] as Boolean) {
            @Suppress("UNCHECKED_CAST")
            init(opts["TARGET"] as ArrayList<String>)
        }
        if (opts["backup"] as Boolean) {
            backup()
        }
        if (opts["restore"] as Boolean) {
            restore()
        }
    }

    private fun requireTargets(targets: List<String>) {
        if (!targets.any()) {
            logger.info("Please provide at least one target")
            logger.info("Available targets: ${config.targets.joinToString { it.id }}")

            return
        }

        logger.info("Chosen targets: ${targets.joinToString { it }}")
    }

    fun init(targets: List<String>) {
        logger.info("init")
        requireTargets(targets)
    }

    fun backup() {
        logger.info("backup")
    }

    fun restore() {
        logger.info("restore")
    }
}