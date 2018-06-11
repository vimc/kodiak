package org.vaccineimpact.kodiak.tests

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.vaccineimpact.kodiak.*
import java.io.File

class KodiakTests : BaseTests() {

    var config = JsonConfig(testConfigSource)
    var mockLogger = mock<Logger>()
    val mockEncryption = mock<Encryption> { on { it.generateEncryptionKey() } doReturn "newfakekey" }
    val mockSecretManager = mock<SecretManager> {
        on {
            it.read("encryption",
                    "key")
        } doReturn "fakekeyfromvault"
        on {
            it.read("aws", "id")
        } doReturn "fakeawsid"
        on {
            it.read("aws", "secret")
        } doReturn "fakeawssecret"
    }
    lateinit var mockBackupTask: BackupTask
    lateinit var mockTaskSource: TaskSource

    lateinit var sut: Kodiak

    @Before
    fun createSut() {
        config = JsonConfig(testConfigSource)
        mockLogger = mock()
        mockBackupTask = mock()
        mockTaskSource = mock {
            on { backupTask(any()) } doReturn mockBackupTask
        }
        sut = Kodiak(config, mockEncryption, mockLogger, mockTaskSource)
    }

    @Test
    fun runsBackup() {
        sut.run(mapOf("backup" to true, "init" to false, "restore" to false))
        verify(mockLogger).info("backup")
        verify(mockTaskSource).backupTask(config)
        verify(mockBackupTask).backup(argForWhich { id == "t1" })
        verify(mockBackupTask).backup(argForWhich { id == "t2" })
        assertThat(File("/tmp/kodiak")).exists()
        File("/tmp/kodiak").delete()
    }

    @Test
    fun runsRestore() {
        sut.run(mapOf("restore" to true, "init" to false, "backup" to false))
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
        assertThat(newConfig.targets).containsExactly(
                Target(
                        id = "t1",
                        encrypted = true,
                        remoteBucket = "testbucket",
                        localPath = "testtarget1"
                )
        )
    }

    @Test
    fun createsEncryptionKeyIfNonExistent() {

        val mockSecretManager =
                mock<SecretManager> {
                    on { it.read("encryption", "key") } doReturn null as String?
                    on { it.read("aws", "id") } doReturn "fakeawsid"
                    on { it.read("aws", "secret") } doReturn "fakeawssecret"
                }

        sut.init(arrayListOf("t1"), mockSecretManager)
        verify(mockSecretManager).write("encryption", "key", "newfakekey")
        assertThat(config.encryptionKey).isEqualTo("newfakekey")
    }

    @Test
    fun usesEncryptionKeyFromVaultIfExistent() {

        sut.init(arrayListOf("t1"), mockSecretManager)
        assertThat(config.encryptionKey).isEqualTo("fakekeyfromvault")
    }

    @Test
    fun throwsMissingSecretExceptionIfCantFindAwsId() {

        val mockSecretManager =
                mock<SecretManager> {
                    on { it.read("aws", "id") } doReturn null as String?
                    on { it.read("aws", "secret") } doReturn "fakesecret"
                }

        assertThatThrownBy { sut.init(arrayListOf("t1"), mockSecretManager) }
                .isInstanceOf(MissingSecret::class.java)
    }

    @Test
    fun throwsMissingSecretExceptionIfCantFindAwsSecret() {

        val mockSecretManager =
                mock<SecretManager> {
                    on { it.read("aws", "id") } doReturn "fakeid"
                    on { it.read("aws", "secret") } doReturn null as String?
                }

        assertThatThrownBy { sut.init(arrayListOf("t1"), mockSecretManager) }
                .isInstanceOf(MissingSecret::class.java)
    }

    @Test
    fun savesAwsConfigOnInit() {
        sut.init(arrayListOf("t1"), mockSecretManager)
        assertThat(config.awsId).isEqualTo("fakeawsid")
        assertThat(config.awsSecret).isEqualTo("fakeawssecret")
    }
}