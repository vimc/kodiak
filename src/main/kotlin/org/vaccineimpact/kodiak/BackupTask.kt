package org.vaccineimpact.kodiak

import org.apache.commons.compress.archivers.ArchiveOutputStream
import org.apache.commons.compress.archivers.ArchiveStreamFactory
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.utils.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.*
import kotlin.concurrent.thread


class BackupTask(val config: Config) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun backup(target: Target): String {
        val source = File(config.starportPath, target.localPath)
        logger.info("Reading from ${source.absolutePath}")

        val stream = source.walk()
                .sortedBy { it.relativeTo(source) }
                .toTarStream(source)
        File("${target.id}.tar").outputStream().use { out ->
            IOUtils.copy(stream, out)
        }
    }
}

fun Sequence<File>.toTarStream(source: File): InputStream {
    val outputStream = PipedOutputStream()
    val inputStream = PipedInputStream(outputStream)
    thread {
        outputStream.use { outputStream ->
            val archiver = ArchiveStreamFactory().createArchiveOutputStream("tar", outputStream)
            for (file in this) {
                archiver.addFile(file, source)
            }
            archiver.finish()
        }
    }
    return inputStream
}

fun ArchiveOutputStream.addFile(file: File, source: File) {
    val relative = file.relativeTo(source)
    println(" - $relative")
    val entry = TarArchiveEntry(file, relative.path)
    this.putArchiveEntry(entry)
    if (file.isFile) {
        IOUtils.copy(file.inputStream(), this)
    }
    this.closeArchiveEntry()
}
