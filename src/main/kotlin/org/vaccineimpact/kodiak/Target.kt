package org.vaccineimpact.kodiak

data class Target(val id: String,
                  val encrypted: Boolean,
                  val remoteBucket: String,
                  val localPath: String)
