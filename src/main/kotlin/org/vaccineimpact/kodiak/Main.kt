package org.vaccineimpact.kodiak

import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger(Kodiak::class.java)!!

fun main(args: Array<String>) {

    val allowedModes = arrayOf("backup", "restore", "init")

    if (args.isEmpty() || !allowedModes.contains(args[0])) {
        logger.info("Please provide a command-line argument of either 'backup', 'restore' or 'init")
        return
    }

    val kodiak = Kodiak(args.drop(1))
    if (args[0] == "init"){
        kodiak.init()
    }
    if (args[0] == "backup"){
        kodiak.backup()
    }
    if (args[0] == "restore"){
        kodiak.restore()
    }
}

class Kodiak(private val targets: List<String>,
             private val config: Config = Config()) {

    private fun requireTargets() {
        if (targets.any()) {
            logger.info("Please provide at least one target")
            logger.info("Available targets: ${config.targets.map{ it.id }}")

            return
        }

        println("Chosen targets: ${targets.map{ it }}")
    }

    fun init() {
        logger.info("init")
        requireTargets()
    }

    fun backup() {
        logger.info("backup")
        requireTargets()
    }

    fun restore() {
        logger.info("restore")
        requireTargets()
    }
}