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
    fun write(){

        sut.write("something", "new", "value")

        val result = sut.read("something", "new")
        assertThat(result).isEqualTo("value")
    }
}