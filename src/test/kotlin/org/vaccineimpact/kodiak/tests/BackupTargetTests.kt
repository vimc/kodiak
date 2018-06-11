package org.vaccineimpact.kodiak.tests

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.vaccineimpact.kodiak.AWSBackupTask
import org.vaccineimpact.kodiak.Config
import org.vaccineimpact.kodiak.Target
import java.io.File

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
        val target = Target("my-target", false, "remote", localPath = targetFolder.path)

        // Test
        val task = AWSBackupTask(config)
        task.backup(target)

        // Expectations
        assertThat(File(working, "my-target.tar")).exists()
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