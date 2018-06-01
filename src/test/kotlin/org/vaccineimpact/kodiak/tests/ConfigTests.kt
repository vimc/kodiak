package org.vaccineimpact.kodiak.tests

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.vaccineimpact.kodiak.JsonConfig

class ConfigTests : BaseTests() {

    private var sut: JsonConfig = JsonConfig(testConfigSource)

    @Before
    fun createConfig() {
        sut = JsonConfig(testConfigSource)
    }

    @Test
    fun canParseStarportPath() {
        assertThat(sut.starportPath).isEqualTo("/tmp/starport")
    }

    @Test
    fun canParseWorkingPath() {
        assertThat(sut.workingPath).isEqualTo("/tmp/kodiak")
    }

    @Test
    fun canParseConfigTargets() {

        val targets = sut.targets
        assertThat(targets.count()).isEqualTo(2)

        val target = targets[0]

        assertThat(target.id).isEqualTo("t1")
        assertThat(target.encrypted).isTrue()
        assertThat(target.localPath).isEqualTo("testtarget1")
        assertThat(target.remoteBucket).isEqualTo("testbucket")
    }

    @Test
    fun savesToFile() {

        // confirm that we're starting with 2 targets
        assertThat(sut.targets.count()).isEqualTo(2)

        // setup: filter the targets
        val filteredTargets = sut.targets.filter({ it.id == "t1" })
        sut.save(filteredTargets, "encryptionkey")

        // test: should now only have 1 target
        assertThat(sut.targets.count()).isEqualTo(1)
        assertThat(sut.encryptionKey).isEqualTo("encryptionkey")

        // test: a newly instantiated config from the same source file should have the same properties
        val newConfig = JsonConfig(testConfigSource)
        assertThat(sut).isEqualTo(newConfig)
    }

}
