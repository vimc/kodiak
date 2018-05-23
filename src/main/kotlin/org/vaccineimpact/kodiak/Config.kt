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

@kotlin.annotation.Target(AnnotationTarget.FIELD)
annotation class Exclude

class AnnotationExclusionStrategy : ExclusionStrategy {

    override fun shouldSkipField(f: FieldAttributes): Boolean {
        return f.getAnnotation(Exclude::class.java) != null
    }

    override fun shouldSkipClass(clazz: Class<*>): Boolean {
        return false
    }
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

    fun save(filteredTargets: List<Target>, encryptionKey: String,
             awsId: String, awsSecret: String) {
        this.targets = filteredTargets
        this.encryptionKey = encryptionKey
        this.awsId = awsId
        this.awsSecret = awsSecret
        val json = this.gson.toJson(this)
        File(this.configPath).writeText(json)
    }

    override val vaultAddress: String = this["vault_address"].asString
    override val starportPath: String = this["starport_path"].asString
    override val workingPath: String = this["working_path"].asString
    override var awsId: String = this.properties["aws_id"]?.asString ?: ""
    override var awsSecret: String = this.properties["aws_secret"]?.asString ?: ""
    override var encryptionKey: String = this.properties["encryption_key"]?.asString ?: ""

    override var targets: List<Target> = gson
            .fromJson<List<Target>>(this["targets"], object : TypeToken<List<Target>>() {}.type)
            .map { it.copy(localPath = "${this.starportPath}/${it.localPath}") }

}

class MissingConfigurationKey(key: String) : Exception("Missing configuration key '$key'")