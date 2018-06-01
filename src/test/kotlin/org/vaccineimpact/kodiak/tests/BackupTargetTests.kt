package org.vaccineimpact.kodiak.tests

import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.vaccineimpact.kodiak.BackupTask
import org.vaccineimpact.kodiak.Config
import org.vaccineimpact.kodiak.RemoteStorage
import org.vaccineimpact.kodiak.Target
import java.io.File
import java.time.*

class BackupTargetTests : BaseTests() {
    @Test
    fun `backup smoke test`() {
        // Setup
        val starport = File("/tmp/starport")
        val working = File("/tmp/kodiak")
        val targetFolder = File("target")
        setupLocalTestFiles(starport, targetFolder)

        val config = mock<Config> {
            on { starportPath } doReturn starport.absolutePath
            on { workingPath } doReturn working.absolutePath
        }
        val s3 = mock<RemoteStorage>()
        val clock = Clock.fixed(LocalDateTime.of(2018, Month.JUNE, 1, 15, 10).toInstant(ZoneOffset.UTC), ZoneId.of("UTC"))
        val target = Target("my-target", false, "remote", localPath = targetFolder.path)

        // Test
        val task = BackupTask(config, s3, clock)
        task.backup(target)

        // Expectations
        verify(s3).backup(
                bucket = eq("montagu-kodiak-my-target"),
                key = eq("2018-06-01T15:10:00Z"),
                stream = any()
        )
    }

    private fun setupLocalTestFiles(starport: File, target: File) {
        val folder = starport.join(target)
        folder.delete()
        folder.mkdirs()
        folder.join("a.txt").writeText("FileA")
        folder.join("b.txt").writeText("FileB")
    }
}

fun File.join(child: File) = File(this, child.path)
fun File.join(child: String) = File(this, child)