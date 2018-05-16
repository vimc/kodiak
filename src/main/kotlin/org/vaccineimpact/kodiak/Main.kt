package org.vaccineimpact.kodiak

import org.slf4j.LoggerFactory
import org.slf4j.Logger

fun main(args: Array<String>) {

    val config = JsonConfig(EnvironmentProperties.configSource)
    val kodiak = Kodiak(config)

    return kodiak.main(args)
}

class Kodiak(private val config: Config,
             private val logger: Logger = LoggerFactory.getLogger(Kodiak::class.java)) {

    private val allowedModes = arrayOf("backup", "restore", "init")

    fun main(args: Array<String>) {
        if (args.isEmpty() || !allowedModes.contains(args[0])) {
            logger.info("Please provide a command-line argument of either 'backup', 'restore' or 'init'")
            return
        }

        if (args[0] == "init"){
            init(args.drop(1))
        }
        if (args[0] == "backup"){
            backup()
        }
        if (args[0] == "restore"){
            restore()
        }
    }

    private fun requireTargets(targets: List<String>) {
        if (!targets.any()) {
            logger.info("Please provide at least one target")
            logger.info("Available targets: ${config.targets.joinToString{ it.id }}")

            return
        }

        logger.info("Chosen targets: ${targets.joinToString{ it }}")
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