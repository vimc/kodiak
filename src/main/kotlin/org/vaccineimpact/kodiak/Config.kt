package org.vaccineimpact.kodiak

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import sun.security.krb5.EncryptionKey
import java.io.File

interface Config {
    val starportPath: String
    val workingPath: String
    val targets: List<Target>
    val awsId: String?
    val awsSecret: String?
    val awsRegion: String?
    val encryptionKey: String?
    val vaultAddress: String

    fun save(filteredTargets: List<Target>, encryptionKey: String, awsId: String, awsSecret: String)
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

    private fun get(key: String): JsonElement {
        val x = properties[key]
        if (x != null) {
            return x
        } else {
            throw MissingConfigurationKey(key)
        }
    }

    private fun getOptional(key: String): JsonElement? = properties[key]

    override fun save(filteredTargets: List<Target>, encryptionKey: String,
             awsId: String, awsSecret: String) {
        this.targets = filteredTargets
        this.encryptionKey = encryptionKey
        this.awsId = awsId
        this.awsSecret = awsSecret
        val json = this.gson.toJson(this)
        File(this.configPath).writeText(json)
    }

    override val vaultAddress: String = get("vault_address").asString
    override val starportPath: String = get("starport_path").asString
    override val workingPath: String = get("working_path").asString

    override var awsId: String? = getOptional("aws_id")?.asString
    override var awsSecret: String? = getOptional("aws_secret")?.asString
    override val awsRegion: String? = getOptional("aws_region")?.asString
    override var encryptionKey: String? = getOptional("encryption_key")?.asString

    override var targets: List<Target> = gson.fromJson<List<Target>>(
            this.get("targets"),
            object : TypeToken<List<Target>>() {}.type
    )

}

class MissingConfigurationKey(key: String) : Exception("Missing configuration key '$key'")