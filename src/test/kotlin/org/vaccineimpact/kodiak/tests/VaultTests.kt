package org.vaccineimpact.kodiak.tests

import org.junit.Ignore
import org.junit.Test
import org.vaccineimpact.kodiak.JsonConfig
import org.vaccineimpact.kodiak.Kodiak
import org.vaccineimpact.kodiak.SodiumEncryption
import org.vaccineimpact.kodiak.VaultManager

class VaultTests: BaseTests() {

    @Test
    @Ignore
    fun read(){

        val config = JsonConfig(testConfigSource)

        // for local testing put your github token here
        val githubToken = ""

        val vault = VaultManager(githubToken, config)
        val result = vault.read("aws", "id")
    }

    @Test
    @Ignore
    fun init(){

        val config = JsonConfig(testConfigSource)

        // for local testing put your github token here
        val githubToken = ""

        val vault = VaultManager(githubToken, config)
        val kodiak = Kodiak(config, SodiumEncryption.instance)
                .init(arrayListOf("test"), vault)
        val result = vault.read("encryption", "key")
    }
}