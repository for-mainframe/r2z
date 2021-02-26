import eu.ibagroup.r2z.JESApi
import eu.ibagroup.r2z.SpoolFile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import retrofit2.Call

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ListSpoolFilesTest : BaseTest(){

  var jesApi: JESApi = buildApi(BASE_URL, getUnsafeOkHttpClient())

  val JOB_CORRELATOR = "J0000440S0W1....D92237DC.......:"
  val JOB_ID = "JOB00440"
  val JOB_NAME = "NOTHINGJ"


  @Test
  fun getListSpoolFilesByCorrelator(){
    val call = jesApi.getJobSpoolFiles(BASIC_AUTH_TOKEN, JOB_CORRELATOR)
    executeCallAndCheckResult(call)
  }

  @Test
  fun getListSpoolFilesByIdAndName(){
    val call = jesApi.getJobSpoolFiles(BASIC_AUTH_TOKEN, JOB_NAME, JOB_ID)
    executeCallAndCheckResult(call)
  }

  fun executeCallAndCheckResult(call: Call<List<SpoolFile>>){
    var response = call.execute()
    if(response.isSuccessful == true){
      var spoolFiles = response.body()
      spoolFiles?.forEach { el-> println(el) }
    } else{
      Assertions.assertTrue(false)
    }
  }
}