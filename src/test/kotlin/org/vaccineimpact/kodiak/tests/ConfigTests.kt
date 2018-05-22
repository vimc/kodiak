package org.vaccineimpact.kodiak.tests

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.vaccineimpact.kodiak.JsonConfig

class ConfigTests : BaseTests() {

    private val sut: JsonConfig = JsonConfig(testConfigSource)

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
        assertThat(targets.count()).isEqualTo(2)

        val target = targets[0]

        assertThat(target.id).isEqualTo("t1")
        assertThat(target.encrypted).isTrue()
        assertThat(target.localPath).isEqualTo("/test/starport/testtarget1")
        assertThat(target.remoteBucket).isEqualTo("testbucket")
    }

    @Test
    fun savesToFile() {

        assertThat(sut.targets.count()).isEqualTo(2)
        val filteredTargets = sut.targets.filter({it.id == "t1"})
        sut.save(filteredTargets)

        val newConfig = JsonConfig(testConfigSource)
        assertThat(newConfig.targets.count()).isEqualTo(1)
        assertThat(newConfig.starportPath).isEqualTo(sut.starportPath)
        assertThat(newConfig.workingPath).isEqualTo(sut.workingPath)
        assertThat(newConfig.awsId).isEqualTo(sut.awsId)
        assertThat(newConfig.awsSecret).isEqualTo(sut.awsSecret)
    }

}
