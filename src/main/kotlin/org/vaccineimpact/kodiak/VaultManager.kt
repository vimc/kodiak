package org.vaccineimpact.kodiak

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import org.slf4j.LoggerFactory

interface SecretManager {
    fun read(name: String, key: String): String?
    fun write(name: String, key: String, value: String)
}

class VaultManager(vaultToken: String, config: Config) : SecretManager {

    private var vaultConfig: VaultConfig = VaultConfig()
            .address(config.vaultAddress)
            .token(vaultToken)
            .build()

    private var vault: Vault = Vault(vaultConfig)

    private val kodiakPath = "secret/kodiak/"

    override fun read(name: String, key: String): String? {
        logger.info("reading secrets from " + kodiakPath)
        val list = vault.logical().list(kodiakPath)
        return if (name in list) {
            vault.logical().read(kodiakPath + name)?.data?.get(key)
        } else {
            null
        }
    }

    override fun write(name: String, key: String, value: String) {
        vault.logical().write(kodiakPath + name, mapOf(key to value))
    }

    companion object {

        val logger = LoggerFactory.getLogger(VaultManager::class.java)

        fun fromGithubAuth(githubToken: String, config: Config): VaultManager {
            val vaultConfigWithoutAuth = VaultConfig()
                    .address(config.vaultAddress)
                    .build()

            val vaultClient = Vault(vaultConfigWithoutAuth)

            logger.info("Connecting to vault at ${config.vaultAddress}")
            val auth = vaultClient.auth().loginByGithub(githubToken)

            return VaultManager(auth.authClientToken, config)
        }
    }
}