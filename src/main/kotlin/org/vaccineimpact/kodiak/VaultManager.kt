package org.vaccineimpact.kodiak

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig

interface SecretManager {
    fun read(name: String, key: String): String?
    fun write(name: String, key: String, value: String)
}

class VaultManager(token: String, config: Config) : SecretManager {

    private val vaultConfig = VaultConfig()
            .address(config.vaultAddress)
            .token(token)
            .build()

    private val vault = Vault(vaultConfig)

    private val kodiakPath = "secret/kodiak/"

    override fun read(name: String, key: String): String? {
        return vault.logical().read(kodiakPath + name)
                .data[key]
    }

    override fun write(name: String, key: String, value: String) {
        vault.logical().write(kodiakPath + name, mapOf(key to value))
    }
}