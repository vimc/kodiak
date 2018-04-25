package org.vaccineimpact.kodiak.tests

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.vaccineimpact.kodiak.JsonConfig
import org.vaccineimpact.kodiak.Kodiak

class KodiakTests : BaseTests() {

    val config = JsonConfig(testConfig)
    var mockLogger = mock<Logger>()
    var sut: Kodiak = Kodiak(config, mockLogger)

    @Before
    fun createSut() {
        mockLogger = mock<Logger>()
        sut = Kodiak(config, mockLogger)
    }

    @Test
    fun requiresTargetsForInit() {

        sut.init(listOf())
        verify(mockLogger).info("Please provide at least one target")
        verify(mockLogger).info("Available targets: t1")
    }

    @Test
    fun logsChosenTargetsOnInit() {

        sut.init(listOf("t1", "t2"))
        verify(mockLogger, never()).info("Please provide at least one target")
        verify(mockLogger).info("init")
        verify(mockLogger).info("Chosen targets: t1, t2")
    }
}