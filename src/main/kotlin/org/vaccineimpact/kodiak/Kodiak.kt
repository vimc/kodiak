package org.vaccineimpact.kodiak

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class Kodiak(private val config: JsonConfig,
             private val encryption: Encryption,
             private val logger: Logger = LoggerFactory.getLogger(Kodiak::class.java)) {

    fun main(opts: Map<String, Any>) {

        if (opts["init"] as Boolean) {

            @Suppress("UNCHECKED_CAST")
            val targets = opts["TARGET"] as ArrayList<String>
            val vaultToken = opts["VAULT_TOKEN"] as String

            val vaultManager = VaultManager(vaultToken, config)
            init(targets, vaultManager)
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


    fun init(targets: ArrayList<String>, secretManager: SecretManager) {

        logger.info("init")
        requireTargets(targets)

        val filteredTargets = config.targets.filter({ targets.contains(it.id) })

        val encryptionKey = secretManager.read("secret/kodiak/encryption", "key")
                ?: encryption.generateEncryptionKey()

        secretManager.write("secret/kodiak/encryption", "key", encryptionKey)
        config.save(filteredTargets, encryptionKey)
    }

    fun backup() {
        logger.info("backup")
    }


    fun restore() {
        logger.info("restore")
    }
}