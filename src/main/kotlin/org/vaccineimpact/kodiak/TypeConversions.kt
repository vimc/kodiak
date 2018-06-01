package org.vaccineimpact.kodiak

import java.util.*

/**
 * This file is for adding extension methods to allow easy conversions between
 * types that do not have conversions in the standard library.
 */

// I posted a question about this here:
// https://stackoverflow.com/questions/50511661/is-there-a-standard-way-to-turn-a-kotlin-sequence-into-a-java-util-enumeration
fun <T> Sequence<T>.toEnumeration(): Enumeration<T> {
    val iterator = this.iterator()
    return object : Enumeration<T> {
        override fun hasMoreElements() = iterator.hasNext()
        override fun nextElement(): T = iterator.next()
    }
}