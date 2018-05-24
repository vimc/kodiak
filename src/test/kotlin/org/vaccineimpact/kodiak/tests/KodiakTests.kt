package org.vaccineimpact.kodiak.tests

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.vaccineimpact.kodiak.JsonConfig
import org.vaccineimpact.kodiak.Kodiak
import org.assertj.core.api.Assertions.assertThat
import org.vaccineimpact.kodiak.Encryption
import org.vaccineimpact.kodiak.SecretManager

class KodiakTests : BaseTests() {

    var config = JsonConfig(testConfigSource)
    var mockLogger = mock<Logger>()
    val mockEncryption = mock<Encryption> { on { it.generateEncryptionKey() } doReturn "newfakekey" }
    val mockSecretManager = mock<SecretManager> {
        on {
            it.read("encryption",
                    "key")
        } doReturn "fakekeyfromvault"
    }
    var sut: Kodiak = Kodiak(config, mockEncryption, mockLogger)

    @Before
    fun createSut() {
        config = JsonConfig(testConfigSource)
        mockLogger = mock<Logger>()
        sut = Kodiak(config, mockEncryption, mockLogger)
    }

    @Test
    fun runsBackup() {
        sut.main(mapOf("backup" to true, "init" to false, "restore" to false))
        verify(mockLogger).info("backup")
    }

    @Test
    fun runsRestore() {
        sut.main(mapOf("restore" to true, "init" to false, "backup" to false))
        verify(mockLogger).info("restore")
    }

    @Test
    fun requiresTargetsForInit() {

        sut.init(arrayListOf(), mockSecretManager)
        verify(mockLogger).info("init")
        verify(mockLogger).info("Please provide at least one target")
        verify(mockLogger).info("Available targets: t1, t2")
    }

    @Test
    fun logsChosenTargetsOnInit() {

        sut.init(arrayListOf("t1", "t2"), mockSecretManager)
        verify(mockLogger, never()).info("Please provide at least one target")
        verify(mockLogger).info("init")
        verify(mockLogger).info("Chosen targets: t1, t2")
    }

    @Test
    fun filtersTargetsOnInit() {

        assertThat(config.targets.count()).isEqualTo(2)
        sut.init(arrayListOf("t1"), mockSecretManager)
        val newConfig = JsonConfig(testConfigSource)
        assertThat(newConfig.targets.count()).isEqualTo(1)
        assertThat(newConfig.targets.all({ it.id == "t1" })).isTrue()
    }

    @Test
    fun createsEncryptionKeyIfNonExistent() {

        val mockSecretManager =
                mock<SecretManager>{ on {it.read("encryption", "key")} doReturn null as String? }
        sut.init(arrayListOf("t1"), mockSecretManager)
        verify(mockSecretManager).write("encryption", "key", "newfakekey")
        assertThat(config.encryptionKey).isEqualTo("newfakekey")
    }

    @Test
    fun usesEncryptionKeyFromVaultIfExistent() {

        sut.init(arrayListOf("t1"), mockSecretManager)
        assertThat(config.encryptionKey).isEqualTo("fakekeyfromvault")
    }
}