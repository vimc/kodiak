package org.vaccineimpact.kodiak.tests

import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.vaccineimpact.kodiak.AWSBackupTask
import org.vaccineimpact.kodiak.Config
import org.vaccineimpact.kodiak.RemoteStorage
import org.vaccineimpact.kodiak.Target
import java.io.File
import java.time.*

class BackupTargetTests : BaseTests() {
    private val starport = File("/tmp/starport")
    private val working = File("/tmp/kodiak")
    private val targetFolder = File("target")

    @Test
    fun `backup smoke test`() {
        // Setup
        val config = mock<Config> {
            on { starportPath } doReturn starport.absolutePath
            on { workingPath } doReturn working.absolutePath
        }
        val s3 = mock<RemoteStorage>()
        val clock = Clock.fixed(LocalDateTime.of(2018, Month.JUNE, 1, 15, 10).toInstant(ZoneOffset.UTC), ZoneId.of("UTC"))
        val target = Target("my-target", false, "remote", localPath = targetFolder.path)

        // Test
        val task = AWSBackupTask(config, s3, clock)
        task.backup(target)

        // Expectations
        verify(s3).backup(
                bucket = eq("montagu-kodiak-my-target"),
                key = eq("2018-06-01T15:10:00Z"),
                stream = any()
        )
    }

    @Before
    fun setupLocalTestFiles() {
        val folder = starport.join(targetFolder)
        folder.delete()
        folder.mkdirs()
        folder.join("a.txt").writeText("FileA")
        folder.join("b.txt").writeText("FileB")
        working.mkdirs()
    }

    @After
    fun deleteDirectories() {
        starport.deleteRecursively()
        working.deleteRecursively()
    }
}

fun File.join(child: File) = File(this, child.path)
fun File.join(child: String) = File(this, child)