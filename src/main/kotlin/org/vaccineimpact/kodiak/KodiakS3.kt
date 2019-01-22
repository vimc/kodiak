package org.vaccineimpact.kodiak

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import java.io.InputStream

interface RemoteStorage {
    fun backup(bucket: String, key: String, stream: InputStream)
}

class KodiakS3(config: Config) : RemoteStorage {
    private val client = makeS3Client(config)

    override fun backup(bucket: String, key: String, stream: InputStream) {
        if (!client.doesBucketExistV2(bucket)) {
            client.createBucket(bucket)
        }
        client.putObject(bucket, key, stream, null)
    }

    companion object {
        private fun makeS3Client(config: Config): AmazonS3 {
            val credentials = ProfileCredentialsProvider()
            return AmazonS3ClientBuilder.standard()
                    .withCredentials(credentials)
                    .apply { region = config.awsRegion }
                    .build()
        }
    }
}