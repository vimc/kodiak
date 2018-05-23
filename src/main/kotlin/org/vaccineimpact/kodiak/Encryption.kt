package org.vaccineimpact.kodiak

import org.abstractj.kalium.NaCl
import java.security.SecureRandom
import java.util.*

interface Encryption {
    fun generateEncryptionKey(): String
}

class SodiumEncryption: Encryption
{
    override fun generateEncryptionKey(): String
    {
        val bytes = ByteArray(NaCl.Sodium.CRYPTO_SECRETBOX_XSALSA20POLY1305_KEYBYTES)
        NaCl.sodium().randombytes(bytes, NaCl.Sodium.CRYPTO_SECRETBOX_XSALSA20POLY1305_KEYBYTES)
        return Base64.getEncoder().encodeToString(bytes)
    }

    companion object {
        val instance = SodiumEncryption()
    }
}