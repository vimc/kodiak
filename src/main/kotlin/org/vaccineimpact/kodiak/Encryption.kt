package org.vaccineimpact.kodiak

import org.abstractj.kalium.NaCl
import java.security.SecureRandom
import java.util.*

interface Encryption {
    fun generateEncryptionKey(): String
}

class SodiumEncryption: Encryption
{
    private val random = SecureRandom.getInstanceStrong()

    override fun generateEncryptionKey(): String
    {
        val bytes = ByteArray(NaCl.Sodium.CRYPTO_SECRETBOX_XSALSA20POLY1305_KEYBYTES)
        random.nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }

    companion object {
        val instance = SodiumEncryption()
    }
}