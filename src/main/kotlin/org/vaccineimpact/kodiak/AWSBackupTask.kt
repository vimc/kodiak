package org.vaccineimpact.kodiak

import org.apache.commons.compress.archivers.ArchiveOutputStream
import org.apache.commons.compress.archivers.ArchiveStreamFactory
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.utils.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.InputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import kotlin.concurrent.thread

interface BackupTask {
    fun backup(target: Target)
}


class AWSBackupTask(val config: Config) : BackupTask {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun backup(target: Target) {
        val source = File(config.starportPath, target.localPath)
        val destination = File(config.workingPath, "${target.id}.tar")
        logger.info("Reading from ${source.absolutePath}")

        val stream = source.walk()
                .sortedBy { it.relativeTo(source) }
                .toTarStream(source)
        destination.outputStream().use { IOUtils.copy(stream, it) }
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
