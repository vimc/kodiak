package org.vaccineimpact.kodiak

import com.google.gson.*
import java.io.FileNotFoundException
import java.io.InputStreamReader
import com.google.gson.reflect.TypeToken
import java.io.File


interface ConfigWrapper {
    val starportPath: String;
    val workingPath: String;
    val targets: List<Target>
}

data class Target(val id: String,
                  val encrypted: Boolean,
                  val remoteBucket: String,
                  val localPath: String)

class Config(configPath: String = "/etc/kodiak/config.json") : ConfigWrapper {

    private val properties: JsonObject
    private val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

    init {

        val file = File(configPath)
        if (!file.exists()) {
            throw FileNotFoundException("No config found at $configPath")
        }
        properties = JsonParser()
                .parse(InputStreamReader(file.inputStream()))
                .asJsonObject
    }

    private fun get(key: String): JsonElement {
        val x = properties[key]
        if (x != null) {
            return x
        } else {
            throw MissingConfigurationKey(key)
        }
    }

    override val starportPath: String = this.get("starport_path").asString
    override val workingPath: String = this.get("working_path").asString

    override val targets: List<Target> = gson
            .fromJson<List<Target>>(this.get("targets"), object : TypeToken<List<Target>>() {}.type)
            .map { it.copy(localPath = "${this.starportPath}/${it.localPath}") }

}

class MissingConfigurationKey(key: String) : Exception("Missing configuration key '$key'")