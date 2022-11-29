## zOSMF Retrofit Library
This library covert zOSMF Rest API with kotlin object oriented code using Retrofit. r2z will allow you to send http requests to your zOSMF.

## Installation
To install this library in your project use one of build tools like Maven, Gradle or Ant. Use the link below to get necessary artifacts.
https://mvnrepository.com/artifact/eu.ibagroup/r2z
```xml
<dependency>
  <groupId>eu.ibagroup</groupId>
  <artifactId>r2z</artifactId>
  <version>{version}</version>
</dependency>
```

## Guide
In r2z you can find ...API classes. They can be used to send requests to zOSMF. Besides API classes there located data classes like Dataset. Their purpose is to wrap a response from the server or a request into it using an object model. let's look at an example.
```kotlin
// Create stub for DataAPI interface using Retrofit. Here baseUrl is url of your zOSMF service.
val dataAPI = Retrofit.Builder()
  .baseUrl(baseUrl)
  .addConverterFactory(GsonConverterFactory.create())
  .client(client)
  .build()
  .create(DataAPI::class.java)

// You can use basic authentication to access zOSMF.
val basicCreds = Credentials.basic(zosmfUser, zosmfPassword) ?: ""

// Call method you need and get Request object.
val request = dataAPI.listDatasetMembers(
  authorizationToken = basicCreds,
  datasetName = "EXAMPLE.DATASET"
)

// Execute request to get Response object.
val response = request.execute()

// If the request went well you can get result from response body.
if (response.isSuccessful){
  val members = response.body();
}
```
Please note that in order to create API stub, you have to specify that the response should be converted by gson. And that's how you can easily use r2z.

## How to run tests

### Unit tests
To run unit tests:
```
./gradlew test -x signArchives
```
### Integration tests
**NOTE:** integration tests use a specific environment. To test their correctness, you need either create the compliant one, or change the tests

Before running integration tests, you need three variables to be set up:
- ``ZOSMF_TEST_URL`` - URL of the real mainframe with z/OSMF API to run the tests
- ``ZOSMF_TEST_USERNAME`` - username with appropriate permissions to run the tests
- ``ZOSMF_TEST_PASSWORD`` - user password to run the tests

To run integration tests:
```
./gradlew intTest
```
