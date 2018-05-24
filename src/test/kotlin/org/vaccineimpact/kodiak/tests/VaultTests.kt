package org.vaccineimpact.kodiak.tests

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test
import org.vaccineimpact.kodiak.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore

@Ignore
class VaultTests: BaseTests() {

    // for local testing put your github token here
    private val githubToken = ""

    @Test
    fun read(){

        val config = JsonConfig(testConfigSource)

        val vault = VaultManager(githubToken, config)
        val result = vault.read("something", "nonexistent")

        assertThat(result).isNull()
    }

    @Test
    fun init(){

        val config = JsonConfig(testConfigSource)

        val vault = VaultManager(githubToken, config)
        val mockEncryption = mock<Encryption>{ on {it.generateEncryptionKey()} doReturn "testkey" }

        Kodiak(config, mockEncryption)
                .init(arrayListOf("test"), vault)

        val result = vault.read("encryption", "key")
        assertThat(result).isEqualTo("testkey")
    }
}