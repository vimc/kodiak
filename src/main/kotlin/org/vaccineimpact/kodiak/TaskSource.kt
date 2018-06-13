package org.vaccineimpact.kodiak

interface TaskSource {
    fun backupTask(config: Config): BackupTask
}

class StandardTaskSource : TaskSource {
    override fun backupTask(config: Config) = AWSBackupTask(config)
}
