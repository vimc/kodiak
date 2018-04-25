package org.vaccineimpact.kodiak.tests

import org.junit.Rule

abstract class BaseTests {

    @get:Rule
    val teamCityIntegration = TeamCityIntegration()

    protected val testConfig = ConfigTests::class.java.classLoader
            .getResource("testconfig.json")
            .openStream()
}