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
        val starport = File("/tmp/starport")
        val targetFolder = File("target")
        setupLocalTestFiles(starport, targetFolder)

        val config = mock<Config> {
            on { starportPath } doReturn starport.absolutePath
        }
        val target = Target("id", false, "remote", localPath = targetFolder.path)
        val task = BackupTask(config)
        assertThat(task.backup(target)).isEqualTo("FileAFileB")
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