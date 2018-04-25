package org.vaccineimpact.kodiak

import org.docopt.Docopt
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.net.URL
import java.util.*

private val doc = (
                "Usage:\n"
                + "  kodiak init [TARGET...]\n"
                + "  kodiak backup\n"
                + "  kodiak restore")


private fun getResource(path: String): URL
{
    val url: URL? = Config::class.java.classLoader.getResource(path)
    if (url != null)
    {
        return url
    }
    else
    {
        throw FileNotFoundException("Unable to load '$path' as a resource steam")
    }
}

fun main(args: Array<String>) {

    val opts = Docopt(doc)
            .parse(args.toList())

    val properties = Properties().apply {
        load(getResource("config.properties").openStream())
    }

    var configPath = properties["config_location"].toString()

    val file = File(configPath)
    if (!file.isAbsolute)
    {
        configPath = Kodiak::class.java.classLoader
                .getResource("config.json").path
    }

    val kodiak = Kodiak(JsonConfig(configPath))

    if (opts["init"] as Boolean){
        @Suppress("UNCHECKED_CAST")
        kodiak.init(opts["TARGET"] as ArrayList<String>)
    }
    if (opts["backup"] as Boolean){
        kodiak.backup()
    }
    if (opts["restore"] as Boolean){
        kodiak.restore()
    }
}

class Kodiak(private val config: Config,
             private val logger: Logger = LoggerFactory.getLogger(Kodiak::class.java)) {

    private fun requireTargets(targets: ArrayList<String>) {
        if (!targets.any()) {
            logger.info("Please provide at least one target")
            logger.info("Available targets: ${config.targets.map{ it.id }}")

            return
        }

        logger.info("Chosen targets: ${targets.map{ it }}")
    }

    fun init(targets: ArrayList<String>) {
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