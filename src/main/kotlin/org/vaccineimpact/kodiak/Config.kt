package org.vaccineimpact.kodiak

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import sun.security.krb5.EncryptionKey
import java.io.File

interface Config {
    val starportPath: String
    val workingPath: String
    val targets: List<Target>
    val awsId: String
    val awsSecret: String
    val encryptionKey: String
    val vaultAddress: String
}

data class Target(val id: String,
                  val encrypted: Boolean,
                  val remoteBucket: String,
                  val localPath: String)

data class FinalConfig(override val starportPath: String,
                       override val workingPath: String,
                       override val targets: List<Target>,
                       override val awsId: String,
                       override val awsSecret: String,
                       override val encryptionKey: String,
                       override val vaultAddress: String) : Config

class JsonConfig(private val configPath: String) : Config {

    private var properties: JsonObject = JsonParser()
            .parse(File(configPath).readText())
            .asJsonObject

    private val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

    private operator fun get(key: String): JsonElement {
        val x = properties[key]
        if (x != null) {
            return x
        } else {
            throw MissingConfigurationKey(key)
        }
    }

    fun toFinalConfig(): FinalConfig {
        return FinalConfig(this.starportPath,
                this.workingPath,
                this.targets,
                this.awsId,
                this.awsSecret,
                this.encryptionKey,
                this.vaultAddress)
    }

    fun save(filteredTargets: List<Target>, encryptionKey: String) {
        val finalConfig = this.toFinalConfig().copy(targets = filteredTargets,
                encryptionKey = encryptionKey)

        File(this.configPath).writeText(this.gson.toJson(finalConfig))

        // update properties from file so that the targets get updated
        this.properties = JsonParser()
                .parse(File(configPath).readText())
                .asJsonObject
    }

    override val vaultAddress: String = this["vault_address"].asString
    override val starportPath: String = this["starport_path"].asString
    override val workingPath: String = this["working_path"].asString
    override val awsId: String = this["aws_id"].asString
    override val awsSecret: String = this["aws_secret"].asString
    override val encryptionKey: String
            get() = this.properties["encryption_key"]?.asString ?: ""

    override val targets: List<Target>
        get() = gson
                .fromJson<List<Target>>(this["targets"], object : TypeToken<List<Target>>() {}.type)
                .map { it.copy(localPath = "${this.starportPath}/${it.localPath}") }

}

class MissingConfigurationKey(key: String) : Exception("Missing configuration key '$key'")