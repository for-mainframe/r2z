/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright © 2021 IBA Group, a.s.
 */

package eu.ibagroup.r2z

import okhttp3.OkHttpClient
import org.junit.jupiter.api.Test


class DataAPITest {

  private val dataAPI = buildGsonApi<DataAPI>(zosmfUrl, UnsafeOkHttpClient.unsafeOkHttpClient)
  private val infoAPI = buildApi<InfoAPI>(zosmfUrl, UnsafeOkHttpClient.unsafeOkHttpClient)

  @Test
  fun testListDatasets() {

    val request = dataAPI.listDataSets(
      xIBMAttr = XIBMAttr(XIBMAttr.Type.BASE, true),
      authorizationToken = basicCreds,
      dsLevel = "DVASS.*"
    )
    val response = request.execute()
    response.body()?.items?.forEach {
      println(it)
    }
    assert(request.isExecuted)
    assert(response.code() == 200)
  }

  @Test
  fun listDatasetsNotInCatalog() {
    val request = dataAPI.listDataSets(
      xIBMAttr = XIBMAttr(XIBMAttr.Type.BASE, true),
      authorizationToken = basicCreds,
      dsLevel = "KIRYL.IJMP.COBOL"
    )
    val response = request.execute()
    response.body()?.items?.forEach {
      println(it)
    }
    assert(request.isExecuted)
    assert(response.code() == 200)
  }

  @Test
  fun testListUss() {
    val request = dataAPI.listUssPath(
      authorizationToken = basicCreds,
      path = "/u/CHP/test-env/test-files/testprog-jcl",
      depth = 1,
      followSymlinks = SymlinkMode.REPORT
    )
    val response = request.execute()
    response.body()?.items?.forEach {
      println(it.isDirectory)
    }
    assert(request.isExecuted)
    assert(response.code() == 200)
  }

  @Test
  fun testListDatasetMembers() {
    val request = dataAPI.listDatasetMembers(
      authorizationToken = basicCreds,
      datasetName = "KIRYL.IJMP.COBOL"
    )
    val response = request.execute()
    assert(request.isExecuted)
    assert(response.code() == 200)
  }

  @Test
  fun testRetrieveDatasetContent() {
    val request = dataAPI.retrieveDatasetContent(
      authorizationToken = basicCreds,
      datasetName = "KIRYL.IJMP.INPUT.ASM.FILEIN",
      volser = "D3DBAR",
      search = "hello",
      insensitive = true,
      xIBMMigratedRecall = MigratedRecall.WAIT
    )
    val response = request.execute()
    assert(response.code() == 200)
  }

  @Test
  fun testWriteContentToDataset() {
    val request = dataAPI.writeToDataset(
      authorizationToken = basicCreds,
      datasetName = "KIRYL.IJMP.INPUT.ASM.FILEIN",
      content = "Hello World 2!"
    )
    val response = request.execute()
    assert(response.code() == 204)
  }

  @Test
  fun testRetrieveMemberContent() {
    val request = dataAPI.retrieveMemberContent(
      authorizationToken = basicCreds,
      datasetName = "KIRYL.IJMP.ASM",
      memberName = "ASAM1",
      volser = "D3SYS1"
    )
    val response = request.execute()
    assert(response.code() == 200)
  }

  @Test
  fun testWriteContentToDatasetMember() {
    val request = dataAPI.writeToDatasetMember(
      authorizationToken = basicCreds,
      datasetName = "KIRYL.IJMP.ASM",
      memberName = "ASAM228",
      content = "Hi There 228"
    )
    val response = request.execute()
    assert(response.code() == 204)
  }

  @Test
  fun testCreateSequentialDataset() {
    val request = dataAPI.createDataset(
      datasetName = "KIRYL.INPFILE.TEST",
      body = CreateDataset(
        allocationUnit = AllocationUnit.TRK,
        recordFormat = RecordFormat.FB,
        datasetOrganization = DatasetOrganization.PS,
        blockSize = 3120,
        primaryAllocation = 10,
        secondaryAllocation = 5,
      ),
      authorizationToken = basicCreds
    )
    val response = request.execute()
    assert(response.code() == 201)
  }

  @Test
  fun testCreatePartitionedDataset() {
    val request = dataAPI.createDataset(
      datasetName = "KIRYL.IJMP.TEST.ASM",
      body = CreateDataset(
        allocationUnit = AllocationUnit.TRK,
        recordFormat = RecordFormat.FB,
        directoryBlocks = 10,
        datasetOrganization = DatasetOrganization.PO,
        blockSize = 3120,
        primaryAllocation = 50,
        secondaryAllocation = 10,
      ),
      authorizationToken = basicCreds
    )
    val response = request.execute()
    assert(response.code() == 201)
  }

  @Test
  fun testDeleteDataset() {
    val request = dataAPI.deleteDataset(
      authorizationToken = basicCreds,
      datasetName = "KIRYL.INPFILE.TEST"
    )
    val response = request.execute()
    assert(response.code() == 204)
  }

  @Test
  fun testRenameDataset() {
    val request = dataAPI.renameDataset(
      authorizationToken = basicCreds,
      toDatasetName = "KIRYL.INPFILE.TEST2",
      body = RenameData(
        fromDataset = RenameData.FromDataset(
          oldDatasetName = "KIRYL.INPFILE.TEST"
        )
      )
    )
    val response = request.execute()
    assert(response.isSuccessful)
  }

  @Test
  fun testRenameDatasetMember() {
    val request = dataAPI.renameDatasetMember(
      authorizationToken = basicCreds,
      toDatasetName = "KIRYL.IJMP.ASM",
      memberName = "ASAM3",
      body = RenameData(
        fromDataset = RenameData.FromDataset(
          oldDatasetName = "KIRYL.IJMP.ASM",
          oldMemberName = "ASAM2"
        )
      )
    )
    println(request.toString())
    val response = request.execute()
    assert(response.isSuccessful)
  }

  @Test
  fun testInfoRequest() {
    val request = infoAPI.getSystemInfo()
    val response = request.execute()
    assert(response.isSuccessful)
  }

  @Test
  fun testCopyDataset() {
    val request = dataAPI.copyToDataset(
      authorizationToken = basicCreds,
      toDatasetName = "KIRYL.IJMP.TEST.ASM",
      body = CopyDataZOS.CopyFromDataset(
        dataset = CopyDataZOS.CopyFromDataset.Dataset(
          datasetName = "KIRYL.IJMP.ASM",
          memberName = ALL_MEMBERS,
        ),
        replace = true
      )
    )
    val response = request.execute()
    assert(response.isSuccessful)
  }

  @Test
  fun testCopyMemberToSequential() {
    val request = dataAPI.copyToDataset(
      authorizationToken = basicCreds,
      toDatasetName = "KIRYL.IJMP.INPUT.COBOL.CUSTFILE",
      body = CopyDataZOS.CopyFromDataset(
        dataset = CopyDataZOS.CopyFromDataset.Dataset(
          datasetName = "KIRYL.IJMP.ASM",
          memberName = "ASAM1",
        )
      )
    )
    val response = request.execute()
    assert(response.isSuccessful)
  }

  @Test
  fun testSequentialToSequential() {
    val request = dataAPI.copyToDataset(
      authorizationToken = basicCreds,
      toDatasetName = "KIRYL.IJMP.INPUT.PLI.TRANFILE",
      toVolser = "D3DBAR",
      body = CopyDataZOS.CopyFromDataset(
        dataset = CopyDataZOS.CopyFromDataset.Dataset(
          datasetName = "KIRYL.IJMP.INPUT.PLI.CUSTFILE"
        )
      )
    )
    val response = request.execute()
    assert(response.isSuccessful)
  }

  @Test
  fun testSequentialToMember() {
    val request = dataAPI.copyToDatasetMember(
      authorizationToken = basicCreds,
      toDatasetName = "KIRYL.IJMP.ASM",
      memberName = "ASAM4",
      body = CopyDataZOS.CopyFromDataset(
        dataset = CopyDataZOS.CopyFromDataset.Dataset(
          datasetName = "KIRYL.IJMP.INPUT.COBOL.TRANFILE"
        )
      )
    )
    val response = request.execute()
    assert(response.isSuccessful)
  }

  @Test
  fun testDeleteMember() {
    val request = dataAPI.deleteDatasetMember(
      authorizationToken = basicCreds,
      datasetName = "KIRYL.IJMP.ASM",
      memberName = "ASAM1"
    )
    val response = request.execute()
    assert(response.isSuccessful)
  }

  @Test
  fun testCopyToMemberFromUssFile() {
    val request = dataAPI.copyToDatasetMemberFromUssFile(
      authorizationToken = basicCreds,
      toDatasetName = "KIRYL.IJMP.ASM",
      memberName = "ASAM5",
      body = CopyDataZOS.CopyFromFile(
        file = CopyDataZOS.CopyFromFile.File(
          fileName = "/u/KIRYL/readme8.md"
        )
      )
    )
    val response = request.execute()
    assert(response.isSuccessful)
  }

  @Test
  fun testCopyToDatasetFromUssFile() {
    val request = dataAPI.copyToDatasetFromUss(
      authorizationToken = basicCreds,
      toDatasetName = "KIRYL.IJMP.INPUT.PLI.TRANFILE",
      body = CopyDataZOS.CopyFromFile(
        file = CopyDataZOS.CopyFromFile.File(
          fileName = "/u/KIRYL/test.txt"
        )
      )
    )
    val response = request.execute()
    errorBodyToList(response.errorBody()!!).forEach { print(it) }
    assert(response.isSuccessful)
  }

  @Test
  fun testCopyToDatasetMemberFromUssFile() {
    val request = dataAPI.copyToDatasetFromUss(
      authorizationToken = basicCreds,
      toDatasetName = "KIRYL.IJMP.ASM(ASAM8)",
      body = CopyDataZOS.CopyFromFile(
        file = CopyDataZOS.CopyFromFile.File(
          fileName = "/u/KIRYL/test.txt"
        )
      )
    )
    val response = request.execute()
    assert(response.isSuccessful)
  }



  @Test
  fun testRetrieveUssFileContent() {
    val request =
      dataAPI.retrieveUssFileContent(authorizationToken = basicCreds, filePath = "u/KIRYL/ijmp/nice.txt")
    val response = request.execute()
    assert(response.isSuccessful)
    print(response.body())
  }

  @Test
  fun testWriteToUssFile() {
    val request = dataAPI.writeToUssFile(
      authorizationToken = basicCreds,
      filePath = "u/KIRYL/ijmp/readme2.md",
      body = "Nice really!"
    )
    val response = request.execute()
    assert(response.isSuccessful)
  }

  @Test
  fun testCreateUssFile() {
    val request = dataAPI.createUssFile(
      authorizationToken = basicCreds,
      filePath = FilePath("/u/KIRYL/ijmp/readme6.md"),
      body = CreateUssFile(
        type = FileType.FILE,
        mode = FileMode(FileModeValue.READ_WRITE, FileModeValue.READ_WRITE, FileModeValue.READ_WRITE)
      )
    )
    val response = request.execute()
    assert(response.isSuccessful)
  }

  @Test
  fun testDeleteUssFile() {
    val request = dataAPI.deleteUssFile(
      authorizationToken = basicCreds,
      filePath = "u/KIRYL/ijmp/nice.txt",
    )
    val response = request.execute()
    assert(response.isSuccessful)
  }

  @Test
  fun changeFileMode() {
    val request = dataAPI.changeFileMode(
      authorizationToken = basicCreds,
      body = ChangeMode(
        mode = FileMode(7,7,7,"-")
      ),
      filePath = FilePath("u/KIRYL/ijmp/readme5.md"),
    )
    val response = request.execute()
    assert(response.isSuccessful)
  }

  @Test
  fun testMoveUssFile() {

    val request = dataAPI.moveUssFile(
      authorizationToken = basicCreds,
      body = MoveUssFile(from = "/u/KIRYL/ijmp/readme3.md"),
      filePath = FilePath("/u/KIRYL/readme6.md"),
    )
    val response = request.execute()
    assert(response.isSuccessful)

  }

  @Test
  fun testCopyUssFileAndDir() {

    val request = dataAPI.copyUssFile(
      authorizationToken = basicCreds,
      body = CopyDataUSS.CopyFromFileOrDir(
        from = "/u/KIRYL/ijmp/readme6.md"
      ),
      filePath = FilePath("/u/KIRYL/readme8.md"),
    )
    val response = request.execute()
    println(response.code())
    assert(response.isSuccessful)

  }

  @Test
  fun testCopyFromZOStoUss() {
    val request = dataAPI.copyDatasetOrMemberToUss(
      authorizationToken = basicCreds,
      body = CopyDataUSS.CopyFromDataset(
        from = CopyDataUSS.CopyFromDataset.Dataset(
          datasetName = "KIRYL.IJMP.INPUT.PLI.CUSTFILE"
        )
      ),
      filePath = FilePath("/u/KIRYL/readme9.md"),
    )
    val response = request.execute()
    println(response.code())
    assert(response.isSuccessful)
  }
  @Test
  fun testCopyPdsMemberToUss() {
    val request = dataAPI.copyDatasetOrMemberToUss(
      authorizationToken = basicCreds,
      body = CopyDataUSS.CopyFromDataset(
        from = CopyDataUSS.CopyFromDataset.Dataset(
          datasetName = "KIRYL.IJMP.ASM",
          memberName = "ASAM2"
        )
      ),
      filePath = FilePath("/u/KIRYL/holly"),
    )
    val response = request.execute()
    assert(response.isSuccessful)
  }

}

