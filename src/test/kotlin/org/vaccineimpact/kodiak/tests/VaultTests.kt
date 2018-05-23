package org.vaccineimpact.kodiak.tests

import org.junit.Ignore
import org.junit.Test
import org.vaccineimpact.kodiak.JsonConfig
import org.vaccineimpact.kodiak.VaultManager

class VaultTests: BaseTests() {

    @Test
    @Ignore
    fun read(){

        val config = JsonConfig(testConfigSource)

        // for local testing put your github token here
        val token = ""

        val vault = VaultManager(token, config)
        val result = vault.read("encryption", "key")
    }
}