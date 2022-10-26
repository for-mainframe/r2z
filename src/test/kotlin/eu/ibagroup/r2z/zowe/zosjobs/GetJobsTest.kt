// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.zosjobs

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import eu.ibagroup.r2z.Job
import eu.ibagroup.r2z.zowe.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.GetJobs
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input.CommonJobParams
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input.GetJobParams
import okhttp3.OkHttpClient
import org.junit.jupiter.api.*
import java.net.InetSocketAddress
import java.net.Proxy
import kotlin.concurrent.thread

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetJobsTest {

  lateinit var mockServer: MockWebServer
  lateinit var proxyClient: OkHttpClient
  lateinit var responseDispatcher: MockResponseDispatcher

  @BeforeAll
  fun createMockServer() {
    mockServer = MockWebServer()
    responseDispatcher = MockResponseDispatcher()
    mockServer.setDispatcher(responseDispatcher)
    thread(start = true) {
      mockServer.play()
    }
    val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(mockServer.hostName, mockServer.port))
    proxyClient = OkHttpClient.Builder().proxy(proxy).build()
  }

  @AfterAll
  fun stopMockServer() {
    mockServer.shutdown()
  }

  @Test
  fun getJobsCommon() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs\\?owner=IBMUSER")) == true ||
            it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs\\?prefix=\\*")) == true ||
            it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs\\?jobid=JOB00023")) == true ||
            it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs\\?owner=testUser")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("getJobs")).setResponseCode(200)
      }
    )
    var jobs = getJobs.getJobsCommon(GetJobParams(owner = "IBMUSER"))
    jobs.forEach { Assertions.assertEquals("IBMUSER", it.owner) }

    jobs = getJobs.getJobsCommon(GetJobParams(prefix = "*"))
    Assertions.assertEquals("JOB00023", jobs[0].jobId)
    Assertions.assertEquals("JOB00024", jobs[1].jobId)

    jobs = getJobs.getJobsCommon(GetJobParams(jobId = "JOB00023"))
    Assertions.assertEquals("TESTJOB2", jobs[0].jobName)

    jobs = getJobs.getJobsCommon(GetJobParams(owner = null))
    Assertions.assertEquals("TESTJOB2", jobs[0].jobName)

    responseDispatcher.clearValidationList()
  }

  @Test
  fun getJob() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs\\?jobid=JOB00023")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("getJob")).setResponseCode(200)
      }
    )
    val job = getJobs.getJob("JOB00023")
    Assertions.assertEquals("TESTJOB2", job.jobName)

    responseDispatcher.clearValidationList()
  }

  @Test
  fun getStatusCommon() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)

    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/BLSJPRMI/STC00052.*")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("getStatus")).setResponseCode(200)
      }
    )
    val job = getJobs.getStatusCommon(CommonJobParams("BLSJPRMI", "STC00052"))
    Assertions.assertEquals("IBMUSER", job.owner)

    responseDispatcher.clearValidationList()
  }

  @Test
  fun getJobs() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs\\?owner=${connection.user}")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("getJobsTest")).setResponseCode(200)
      }
    )
    val jobs = getJobs.getJobs()
    jobs.forEach { Assertions.assertEquals(connection.user, it.owner) }

    responseDispatcher.clearValidationList()
  }

  @Test
  fun getStatusValue() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)

    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/BLSJPRMI/STC00052.*")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("getStatus")).setResponseCode(200)
      }
    )
    val status = getJobs.getStatusValue("BLSJPRMI", "STC00052")
    Assertions.assertEquals("OUTPUT", status)

    responseDispatcher.clearValidationList()
  }

  @Test
  fun getStatusValueForJob() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)

    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/BLSJPRMI/STC00052.*")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("getStatus")).setResponseCode(200)
      }
    )
    val status = getJobs.getStatusValueForJob(
      Job(
        jobId = "STC00052",
        jobName = "BLSJPRMI",
        subSystem = "IBMUSER",
        owner = "IBMUSER",
        type = Job.JobType.STC,
        url = "https://host:port/zosmf/restjobs/jobs/S0000052SY1.....CE35BDE8.......%3A",
        filesUrl = "https://host:port/zosmf/restjobs/jobs/S0000052SY1.....CE35BDE8.......%3A/files",
        phase = 20,
        phaseName = "Job is on the hard copy queue"
        )
    )
    Assertions.assertEquals("OUTPUT", status)

    responseDispatcher.clearValidationList()
  }

  @Test
  fun getStatus() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/BLSJPRMI/STC00052.*")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("getStatus")).setResponseCode(200)
      }
    )
    val job = getJobs.getStatus(jobId = "STC00052", jobName = "BLSJPRMI")
    Assertions.assertEquals("BLSJPRMI", job.jobName)
    Assertions.assertEquals("STC00052", job.jobId)
    Assertions.assertEquals("OUTPUT", job.status?.value)
    Assertions.assertEquals("CC 0000", job.returnedCode)
    responseDispatcher.clearValidationList()
  }

  @Test
  fun getStatusForJob() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/BLSJPRMI/STC00052.*")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("getStatus")).setResponseCode(200)
      }
    )
    val job = getJobs.getStatusForJob(Job(
      jobId = "STC00052",
      jobName = "BLSJPRMI",
      owner = "IBMUSER",
      type = Job.JobType.STC,
      url = "https://host:port/zosmf/restjobs/jobs/S0000052SY1.....CE35BDE8.......%3A",
      filesUrl = "https://host:port/zosmf/restjobs/jobs/S0000052SY1.....CE35BDE8.......%3A/files",
      phase = 20,
      phaseName = "Job is on the hard copy queue"
    ))
    Assertions.assertEquals("OUTPUT", job.status?.value)
    Assertions.assertEquals("STC", job.type.value)
    Assertions.assertEquals(20, job.phase)
  }

  @Test
  fun getJobsByOwner() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs\\?owner=ZOSMFAD")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("getJobsByOwner")).setResponseCode(200)
      }
    )
    val jobs = getJobs.getJobsByOwner("ZOSMFAD")
    jobs.forEach { Assertions.assertEquals("ZOSMFAD", it.owner) }

    responseDispatcher.clearValidationList()
  }

  @Test
  fun getJobsByPrefix() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs\\?prefix=IJMP\\*")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("getJobsByPrefix")).setResponseCode(200)
      }
    )
    val jobs = getJobs.getJobsByPrefix("IJMP*")
    jobs.forEach { Assertions.assertEquals(true, it.jobName.contains("IJMP")) }

    responseDispatcher.clearValidationList()
  }

  @Test
  fun getJobsByOwnerAndPrefix() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs\\?owner=ZOSMFAD&prefix=\\*")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("getJobsByOwnerAndPrefix")).setResponseCode(200)
      }
    )
    val jobs = getJobs.getJobsByOwnerAndPrefix("ZOSMFAD" ,"*")
    jobs.forEach { Assertions.assertEquals("ZOSMFAD", it.owner) }
    Assertions.assertEquals(6, jobs.size)

    responseDispatcher.clearValidationList()
  }

  @Test
  fun getSpoolFilesCommon() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/NBEL/TSU00555/files")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("getSpoolFiles")).setResponseCode(200)
      }
    )
    val spoolFiles = getJobs.getSpoolFilesCommon(CommonJobParams(jobName = "NBEL", jobId = "TSU00555"))
    spoolFiles.forEach {
      Assertions.assertEquals("NBEL", it.jobname)
      Assertions.assertEquals("TSU00555", it.jobId)
    }

    responseDispatcher.clearValidationList()
  }

  @Test
  fun getSpoolFilesForJob() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/NBEL/TSU00555/files")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("getSpoolFiles")).setResponseCode(200)
      }
    )
    val spoolFiles = getJobs.getSpoolFilesForJob(Job(
      jobId = "TSU00555",
      jobName = "NBEL",
      subSystem = "JES2",
      owner = "NBEL",
      type = Job.JobType.TSU,
      url = "https://host:port/zosmf/restjobs/jobs/T0000555S0W1....DB9E6D9D.......%3A",
      filesUrl = "https://host:port/zosmf/restjobs/jobs/T0000555S0W1....DB9E6D9D.......%3A/files",
      phase = 14,
      phaseName = "Job is actively executing"
    ))
    spoolFiles.forEach {
      Assertions.assertEquals("NBEL", it.jobname)
      Assertions.assertEquals("TSU00555", it.jobId)
    }

    responseDispatcher.clearValidationList()
  }

  @Test
  fun getSpoolFiles() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/NBEL/TSU00555/files")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("getSpoolFiles")).setResponseCode(200)
      }
    )
    val spoolFiles = getJobs.getSpoolFiles("NBEL", "TSU00555")
    spoolFiles.forEach {
      Assertions.assertEquals("NBEL", it.jobname)
      Assertions.assertEquals("JES2", it.stepName)
      Assertions.assertEquals("K", it.fileClass)
    }
  }

  @Test
  fun getJclCommon() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)
    val responseBody = javaClass.classLoader.getResource("mock/getJcl.txt")?.readText()
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/NBEL/TSU00555/files/JCL/records\\?mode=text")) == true
      },
      {
        MockResponse().setBody(responseBody).setResponseCode(200)
      }
    )
    val jcl = getJobs.getJclCommon(CommonJobParams(jobName = "NBEL", jobId = "TSU00555"))
    Assertions.assertEquals(jcl.contains("NBEL"), true)
    Assertions.assertEquals(jcl.contains("TSU00555"), true)
  }

  @Test
  fun getJcl() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)
    val responseBody = javaClass.classLoader.getResource("mock/getJcl.txt")?.readText()
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/NBEL/TSU00555/files/JCL/records\\?mode=text")) == true
      },
      {
        MockResponse().setBody(responseBody).setResponseCode(200)
      }
    )
    val jcl = getJobs.getJcl(jobName = "NBEL", jobId = "TSU00555")
    Assertions.assertEquals(jcl.contains("NBEL"), true)
    Assertions.assertEquals(jcl.contains("TSU00555"), true)
  }

  @Test
  fun getJclForJob() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)
    val responseBody = javaClass.classLoader.getResource("mock/getJcl.txt")?.readText()
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/NBEL/TSU00555/files/JCL/records.*")) == true
      },
      {
        MockResponse().setBody(responseBody).setResponseCode(200)
      }
    )
    val jcl = getJobs.getJclForJob(
      Job(
        jobId = "TSU00555",
        jobName = "NBEL",
        subSystem = "JES2",
        owner = "NBEL",
        type = Job.JobType.TSU,
        url = "https://host:port/zosmf/restjobs/jobs/T0000555S0W1....DB9E6D9D.......%3A",
        filesUrl = "https://host:port/zosmf/restjobs/jobs/T0000555S0W1....DB9E6D9D.......%3A/files",
        phase = 14,
        phaseName = "Job is actively executing"
      )
    )
    Assertions.assertEquals(jcl.contains("NBEL"), true)
    Assertions.assertEquals(jcl.contains("TSU00555"), true)
  }

  @Test
  fun getSpoolContent() {
    // TODO: implement!!! Use getSpoolContent mock.
  }

  @Test
  fun getSpoolContentById() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val getJobs = GetJobs(connection, proxyClient)
    val responseBody = javaClass.classLoader.getResource("mock/getSpoolFileContent.txt")?.readText()
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/TESTJCL/JOB09502/files/2/records.*")) == true
      },
      {
        MockResponse().setBody(responseBody).setResponseCode(200)
      }
    )
    val spoolFile = getJobs.getSpoolContentById("TESTJCL", "JOB09502", 2)
    Assertions.assertEquals(spoolFile.contains("J E S 2  J O B  L O G"), true)
    Assertions.assertEquals(spoolFile.contains("TESTJCL"), true)
    Assertions.assertEquals(spoolFile.contains("JOB09502"), true)
  }

  @Test
  fun getSpoolContentCommon () {
    // TODO: implement!!! Use getSpoolContent mock.
  }

}