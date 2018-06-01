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
    val encryptionKey: String?
    val vaultAddress: String

    fun save(filteredTargets: List<Target>, encryptionKey: String)
}

data class JsonConfig(private val configPath: String) : Config {

    @Exclude
    private var properties: JsonObject = JsonParser()
            .parse(File(configPath).readText())
            .asJsonObject

    @Exclude
    private val gson = GsonBuilder()
            .addSerializationExclusionStrategy(AnnotationExclusionStrategy())
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

    override fun save(filteredTargets: List<Target>, encryptionKey: String) {
        this.targets = filteredTargets
        this.encryptionKey = encryptionKey
        val json = this.gson.toJson(this)
        File(this.configPath).writeText(json)
    }

    override val vaultAddress: String = this["vault_address"].asString
    override val starportPath: String = this["starport_path"].asString
    override val workingPath: String = this["working_path"].asString
    override val awsId: String = this["aws_id"].asString
    override val awsSecret: String = this["aws_secret"].asString
    override var encryptionKey: String? = this.properties["encryption_key"]?.asString

    override var targets: List<Target> = gson.fromJson<List<Target>>(
            this["targets"],
            object : TypeToken<List<Target>>() {}.type
    )

}

class MissingConfigurationKey(key: String) : Exception("Missing configuration key '$key'")