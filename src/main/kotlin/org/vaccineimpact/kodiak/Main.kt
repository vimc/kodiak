package org.vaccineimpact.kodiak

import org.docopt.Docopt
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val doc = (
        """Usage:
        |  kodiak init [VAULT_TOKEN] [TARGET...]
        |  kodiak backup
        |  kodiak restore""".trimMargin())


fun main(args: Array<String>) {

    val opts = Docopt(doc)
            .parse(args.toList())

    val config = JsonConfig(EnvironmentProperties.configSource)
    val kodiak = Kodiak(config)

    return kodiak.main(opts)
}

class Initialiser(private val config: JsonConfig,
                  private val encryption: Encryption,
                  private val vaultManager: VaultManager,
                  private val targets: List<String>,
                  private val logger: Logger = LoggerFactory.getLogger(Initialiser::class.java)) {

    private fun requireTargets(targets: List<String>) {
        if (!targets.any()) {
            logger.info("Please provide at least one target")
            logger.info("Available targets: ${config.targets.joinToString { it.id }}")

            return
        }

        logger.info("Chosen targets: ${targets.joinToString { it }}")
    }

    fun init() {

        logger.info("init")
        requireTargets(targets)

        val filteredTargets = config.targets.filter({ targets.contains(it.id) })

        val encryptionKey = vaultManager.read("secret/kodiak/encryption", "key")
                ?: encryption.generateEncryptionKey()

        vaultManager.write("secret/kodiak/encryption", "key", encryptionKey)
        config.save(filteredTargets, encryptionKey)
    }
}

class Kodiak(private val config: JsonConfig,
             private val logger: Logger = LoggerFactory.getLogger(Kodiak::class.java)) {

    fun main(opts: Map<String, Any>) {

        if (opts["init"] as Boolean) {
            @Suppress("UNCHECKED_CAST")
            init(opts["TARGET"] as ArrayList<String>, opts["VAULT_TOKEN"] as String)
        }
        if (opts["backup"] as Boolean) {
            backup()
        }
        if (opts["restore"] as Boolean) {
            restore()
        }
    }


    fun init(targets: ArrayList<String>, vaultToken: String) {

        logger.info("init")
        Initialiser(config,
                SodiumEncryption.instance,
                VaultManager(vaultToken, config),targets).init()
    }

    fun backup() {
        logger.info("backup")
    }


    fun restore() {
        logger.info("restore")
    }
}