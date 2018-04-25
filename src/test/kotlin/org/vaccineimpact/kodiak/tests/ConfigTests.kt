package org.vaccineimpact.kodiak.tests

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.vaccineimpact.kodiak.JsonConfig
import org.vaccineimpact.kodiak.KodiakConfig


class ConfigTests : BaseTests() {

    private val sut: KodiakConfig = JsonConfig(testConfig)

    @Test
    fun canParseStarportPath() {
        assertThat(sut.starportPath).isEqualTo("/test/starport")
    }

    @Test
    fun canParseWorkingPath() {
        assertThat(sut.workingPath).isEqualTo("/test/kodiak")
    }

    @Test
    fun canParseConfigTargets() {

        val targets = sut.targets
        assertThat(targets.count()).isEqualTo(1)

        val target = targets[0]

        assertThat(target.id).isEqualTo("t1")
        assertThat(target.encrypted).isTrue()
        assertThat(target.localPath).isEqualTo("/test/starport/testtarget")
        assertThat(target.remoteBucket).isEqualTo("testbucket")
    }

}
