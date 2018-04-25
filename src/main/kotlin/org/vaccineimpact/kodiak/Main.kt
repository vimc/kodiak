package org.vaccineimpact.kodiak

import org.docopt.Docopt
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger(Kodiak::class.java)!!

private val doc = (
                "Usage:\n"
                + "  kodiak init\n"
                + "  kodiak backup\n"
                + "  kodiak restore")

fun main(args: Array<String>) {

    val opts = Docopt(doc)
            .parse(args.toList())

    val kodiak = Kodiak(args.drop(1))

    if (opts["init"] as Boolean){
        kodiak.init()
    }
    if (opts["backup"] as Boolean){
        kodiak.backup()
    }
    if (opts["restore"] as Boolean){
        kodiak.restore()
    }
}

class Kodiak(private val targets: List<String>,
             private val config: Config = JsonConfig()) {

    private fun requireTargets() {
        if (targets.any()) {
            logger.info("Please provide at least one target")
            logger.info("Available targets: ${config.targets.map{ it.id }}")

            return
        }

        val targets = args.drop(1)
        println("Chosen targets: ${targets.map{ it }}")
    }

    println("TODO: ${args[0]}")
}