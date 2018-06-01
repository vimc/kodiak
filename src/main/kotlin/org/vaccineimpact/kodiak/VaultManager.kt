package org.vaccineimpact.kodiak

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import org.slf4j.LoggerFactory

interface SecretManager {
    fun read(name: String, key: String): String?
    fun write(name: String, key: String, value: String)
}

class VaultManager(githubToken: String, config: Config) : SecretManager {

    private var vaultConfig = VaultConfig()
            .address(config.vaultAddress)
            .build()

    private val logger = LoggerFactory.getLogger(VaultManager::class.java)

    private var vault = Vault(vaultConfig)

    init {

        logger.info("Connecting to vault at ${config.vaultAddress}")
        val auth = vault.auth().loginByGithub(githubToken)

        // now rebuild the vault config with the token obtained by the github login
        vaultConfig = VaultConfig()
                .address(config.vaultAddress)
                .token(auth.authClientToken)
                .build()

        // and rebuild the vault client with the new config
        vault = Vault(vaultConfig)
    }

    private val kodiakPath = "secret/kodiak/"

    override fun read(name: String, key: String): String? {
        logger.info("reading secrets from " + kodiakPath)
        val list = vault.logical().list(kodiakPath)
        logger.info(list.joinToString())
        return if (!list.contains(name)) {
            null
        } else {
            vault.logical().read(kodiakPath + name)?.data?.get(key)
        }
    }

    override fun write(name: String, key: String, value: String) {
        vault.logical().write(kodiakPath + name, mapOf(key to value))
    }
}