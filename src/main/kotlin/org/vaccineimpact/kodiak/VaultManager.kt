package org.vaccineimpact.kodiak

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig

interface SecretManager {
    fun read(name: String, key: String): String?
    fun write(name: String, key: String, value: String)
}

class VaultManager(githubToken: String, config: Config) : SecretManager {

    private val vaultConfig = VaultConfig()
            .address(config.vaultAddress)
            .build()

    private val vault = Vault(vaultConfig)

    init {
        vault.auth().loginByGithub(githubToken)
    }

    private val kodiakPath = "secret/kodiak/"

    override fun read(name: String, key: String): String? {
        val list = vault.logical().list(kodiakPath)
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