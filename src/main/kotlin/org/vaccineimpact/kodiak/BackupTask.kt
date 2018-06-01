package org.vaccineimpact.kodiak

import org.apache.commons.compress.archivers.ArchiveOutputStream
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import org.apache.commons.compress.utils.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.*
import kotlin.concurrent.thread


class BackupTask(val config: Config) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun backup(target: Target) {
        val source = File(config.starportPath, target.localPath)
        val destination = File(config.workingPath, "${target.id}.tar.gz")
        logger.info("Reading from ${source.absolutePath}")

        val stream = source.walk()
                .sortedBy { it.relativeTo(source) }
                .toCompressedTarStream(source)
        destination.outputStream().use { IOUtils.copy(stream, it) }
    }
}

fun Sequence<File>.toCompressedTarStream(source: File): InputStream {
    val outputStream = PipedOutputStream()
    val inputStream = PipedInputStream(outputStream)
    thread {
        outputStream.buffered().gzip().tar().use { archiver ->
            for (file in this) {
                archiver.addFile(file, source)
            }
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

fun OutputStream.gzip() = GzipCompressorOutputStream(this)

fun OutputStream.tar() = TarArchiveOutputStream(this)
