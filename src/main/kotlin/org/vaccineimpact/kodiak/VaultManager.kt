package org.vaccineimpact.kodiak

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig

class VaultManager(token: String, config: Config) {

    private val vaultConfig = VaultConfig()
            .address(config.vaultAddress)
            .token(token)
            .build()

    private val vault = Vault(vaultConfig)

    fun read(path: String, key: String): String? {
        return vault.logical().read(path)
                .data[key]
    }

    fun write(path: String, key: String, value: String) {
        vault.logical().write(path, mapOf(key to value))
    }
}