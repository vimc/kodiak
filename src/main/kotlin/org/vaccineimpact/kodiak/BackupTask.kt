package org.vaccineimpact.kodiak

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.SequenceInputStream

class BackupTask {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun backup(target: Target, config: Config) {
        val source = File(config.starportPath, target.localPath)
        logger.info("Reading from ${source.absolutePath}")
        val files = source.walk()
                .filter { it.isFile }
                .sortedBy { it.relativeTo(source) }
        val stream = SequenceInputStream(files.toEnumeration { it.inputStream() })
        //This is just a placeholder until we add the next bit on
        println(stream.bufferedReader().readText())
    }
}
