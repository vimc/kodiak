package org.vaccineimpact.kodiak

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import java.io.InputStreamReader

interface Config {
    val starportPath: String;
    val workingPath: String;
    val targets: List<Target>
}

data class Target(val id: String,
                  val encrypted: Boolean,
                  val remoteBucket: String,
                  val localPath: String)

class JsonConfig(inputStream: InputStream) : Config {

    private val properties: JsonObject = JsonParser()
            .parse(InputStreamReader(inputStream))
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

    override val starportPath: String = this["starport_path"].asString
    override val workingPath: String = this["working_path"].asString

    override val targets: List<Target> = gson
            .fromJson<List<Target>>(this["targets"], object : TypeToken<List<Target>>() {}.type)
            .map { it.copy(localPath = "${this.starportPath}/${it.localPath}") }

}

class MissingConfigurationKey(key: String) : Exception("Missing configuration key '$key'")