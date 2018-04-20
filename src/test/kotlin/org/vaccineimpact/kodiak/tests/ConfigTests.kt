package org.vaccineimpact.kodiak.tests

import org.assertj.core.api.Assertions.*
import org.junit.Test
import org.vaccineimpact.kodiak.Config


class ConfigTests {

    val sut = Config("/etc/kodiak/testconfig.json")

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
        assertThat(target.id).isEqualTo("test")
        assertThat(target.encrypted).isTrue()
        assertThat(target.localPath).isEqualTo("/test/starport/testtarget")
        assertThat(target.remoteBucket).isEqualTo("testbucket")
    }

}
