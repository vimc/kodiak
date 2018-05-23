package org.vaccineimpact.kodiak

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig

interface SecretManager {
    fun read(path: String, key: String): String?
    fun write(path: String, key: String, value: String)
}

class VaultManager(token: String, config: Config) : SecretManager {

    private val vaultConfig = VaultConfig()
            .address(config.vaultAddress)
            .token(token)
            .build()

    private val vault = Vault(vaultConfig)

    override fun read(path: String, key: String): String? {
        return vault.logical().read(path)
                .data[key]
    }

    override fun write(path: String, key: String, value: String) {
        vault.logical().write(path, mapOf(key to value))
    }
}