package org.vaccineimpact.kodiak.tests

import com.bettercloud.vault.VaultException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.docopt.DocoptExitException
import org.junit.Test
import org.vaccineimpact.kodiak.main

class DocoptTests : BaseTests() {

    @Test
    fun initRequiresVaultToken() {

        assertThatThrownBy { main(arrayOf("init", "t1")) }
                .isInstanceOf(DocoptExitException::class.java)

        // this will still throw an exception because we have provided a
        // nonsense vault token
        assertThatThrownBy { main(arrayOf("init", "--vault-token=token", "t1")) }
                .isNotInstanceOf(DocoptExitException::class.java)
    }
}