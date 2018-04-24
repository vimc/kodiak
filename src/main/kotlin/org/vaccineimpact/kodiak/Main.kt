package org.vaccineimpact.kodiak

import org.slf4j.LoggerFactory

fun main(args: Array<String>) {

    val config = Config()
    Kodiak().init()
    println("Available targets: ${config.targets.map{ it.id }}")

    val allowedModes = arrayOf("backup", "restore", "init")

    if (args.isEmpty() || !allowedModes.contains(args[0])) {
        println("Please provide a command-line argument of either 'backup', 'restore' or 'init")
        return
    }

    if (args[0] == "init"){
        if (args.count() == 1) {
            println("Please provide at least one target")
            return
        }

        val targets = args.drop(1)
        println("Chosen targets: ${targets.map{ it }}")
    }

    println("TODO: ${args[0]}")
}

class Kodiak {
    private val logger = LoggerFactory.getLogger(Kodiak::class.java)

    fun init() {
        logger.debug("Hello world")
    }
}