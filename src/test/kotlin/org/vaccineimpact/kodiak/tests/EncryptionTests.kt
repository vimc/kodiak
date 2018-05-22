package org.vaccineimpact.kodiak.tests

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.vaccineimpact.kodiak.SodiumEncryption

class EncryptionTests : BaseTests() {

    private val sut = SodiumEncryption()

    @Test
    fun createsEncryptionKey() {
        val key = sut.generateEncryptionKey()
        assertThat(key.length).isGreaterThan(0)
    }
}