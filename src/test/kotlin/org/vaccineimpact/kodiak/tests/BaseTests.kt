package org.vaccineimpact.kodiak.tests

import org.junit.Before
import org.junit.Rule
import java.io.File

abstract class BaseTests {

    @get:Rule
    val teamCityIntegration = TeamCityIntegration()

    protected val testConfigSource = ConfigTests::class.java.classLoader
            .getResource("testconfig.json")
            .path

    @Before
    fun createConfigFile() {
        // rewrite config file before each test
        // as some tests modify the file itself
        File(testConfigSource).writeText("""{
              "starport_path": "/tmp/starport",
              "working_path": "/tmp/kodiak",
              "vault_address": "http://localhost:1234",
              "aws_id": "id",
              "aws_secret": "secret",
              "aws_region": "eu-west-2",
              "targets": [
                {
                  "id": "test-a",
                  "encrypted": true,
                  "remote_bucket": "testbucket",
                  "local_path": "testtarget_a"
                },
                {
                  "id": "test-b",
                  "encrypted": true,
                  "remote_bucket": "testbucket",
                  "local_path": "testtarget_b"
                }
              ]
            }""")
    }

}