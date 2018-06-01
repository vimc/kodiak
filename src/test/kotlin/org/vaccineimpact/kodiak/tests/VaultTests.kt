package org.vaccineimpact.kodiak.tests

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test
import org.vaccineimpact.kodiak.*
import org.assertj.core.api.Assertions.assertThat

class VaultTests: BaseTests() {

    private val mockConfig = mock<Config>{ on {vaultAddress} doReturn "http://127.0.0.1:1234"}
    private val sut = VaultManager("myroot", mockConfig)

    @Test
    fun read(){

        val result = sut.read("something", "nonexistent")
        assertThat(result).isNull()
    }

    @Test
    fun init(){

        val mockEncryption = mock<Encryption>{ on {it.generateEncryptionKey()} doReturn "testkey" }

        Kodiak(mockConfig, mockEncryption)
                .init(arrayListOf("test"), sut)

        val result = sut.read("encryption", "key")
        assertThat(result).isEqualTo("testkey")
    }
}