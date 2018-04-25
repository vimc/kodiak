package org.vaccineimpact.kodiak.tests

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.vaccineimpact.kodiak.JsonConfig
import org.vaccineimpact.kodiak.Kodiak
import org.vaccineimpact.kodiak.main

class KodiakTests: BaseTests() {

    private var mockLogger = mock<Logger>()
    private var sut = Kodiak(config = JsonConfig(testConfigPath), logger = mockLogger)

    @Before
    fun createSut(){
        mockLogger = mock<Logger>()
        sut = Kodiak(config = JsonConfig(testConfigPath), logger = mockLogger)
    }

    @Test
    fun callsBackup() {

        main(arrayOf("backup"))
    }

    @Test
    fun requiresTargetsOnInit() {

        sut.init(arrayListOf())
        verify(mockLogger, never()).info("Please provide at least one target")

        sut.init(arrayListOf())
        verify(mockLogger).info("Please provide at least one target")

    }

}