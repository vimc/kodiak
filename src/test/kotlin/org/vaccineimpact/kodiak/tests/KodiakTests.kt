package org.vaccineimpact.kodiak.tests

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.vaccineimpact.kodiak.JsonConfig
import org.vaccineimpact.kodiak.Kodiak
import org.assertj.core.api.Assertions.assertThat

class KodiakTests : BaseTests() {

    val config = JsonConfig(testConfigSource)
    var mockLogger = mock<Logger>()
    var sut: Kodiak = Kodiak(config, mockLogger)

    @Before
    fun createSut() {
        mockLogger = mock<Logger>()
        sut = Kodiak(config, mockLogger)
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
    fun runsInit() {
        sut.main(mapOf("init" to true, "backup" to false,
                "restore" to false, "TARGET" to arrayListOf("target1")))
        verify(mockLogger).info("init")
    }

    @Test
    fun requiresTargetsForInit() {

        sut.init(listOf())
        verify(mockLogger).info("Please provide at least one target")
        verify(mockLogger).info("Available targets: t1, t2")
    }

    @Test
    fun logsChosenTargetsOnInit() {

        sut.init(listOf("t1", "t2"))
        verify(mockLogger, never()).info("Please provide at least one target")
        verify(mockLogger).info("init")
        verify(mockLogger).info("Chosen targets: t1, t2")
    }

    @Test
    fun filtersTargetsOnInit() {

        assertThat(config.targets.count()).isEqualTo(2)
        sut.init(listOf("t1"))
        val newConfig = JsonConfig(testConfigSource)
        assertThat(newConfig.targets.count()).isEqualTo(1)
        assertThat(newConfig.targets.all({it.id == "t1"})).isTrue()
    }
}