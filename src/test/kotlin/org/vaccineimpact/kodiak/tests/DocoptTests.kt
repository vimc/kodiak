package org.vaccineimpact.kodiak.tests

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.docopt.Docopt
import org.docopt.DocoptExitException
import org.junit.Test
import org.vaccineimpact.kodiak.doc

class DocoptTests : BaseTests() {

    @Test
    fun initRequiresVaultToken() {

        assertThatThrownBy {
            Docopt(doc)
                    .withExit(false)
                    .parse(listOf("init", "t1"))
        }
                .isInstanceOf(DocoptExitException::class.java)

        Docopt(doc)
                .parse(listOf("init", "--vault-token=token", "t1"))
    }
}