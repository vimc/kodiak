package org.vaccineimpact.kodiak.tests

import org.junit.Rule

abstract class BaseTests {

    protected val testConfigPath = ConfigTests::class.java.classLoader
            .getResource("testconfig.json")
            .path

    @get:Rule
    val teamCityIntegration = TeamCityIntegration()

}