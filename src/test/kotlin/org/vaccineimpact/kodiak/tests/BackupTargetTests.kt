package org.vaccineimpact.kodiak.tests

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.vaccineimpact.kodiak.BackupTask
import org.vaccineimpact.kodiak.Config
import org.vaccineimpact.kodiak.Target
import java.io.File

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
        val target = Target("my-target", false, "remote", localPath = targetFolder.path)

        // Test
        val task = BackupTask(config)
        task.backup(target)

        // Expectations
        assertThat(File(working, "my-target.tar")).exists()
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