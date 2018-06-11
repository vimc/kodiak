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
              "targets": [
                {
                  "id": "t1",
                  "encrypted": true,
                  "remote_bucket": "testbucket",
                  "local_path": "testtarget1"
                },
                {
                  "id": "t2",
                  "encrypted": true,
                  "remote_bucket": "testbucket",
                  "local_path": "testtarget2"
                }
              ]
            }""")
    }

}