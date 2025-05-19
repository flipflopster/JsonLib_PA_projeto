# JsonLib

This library is Json In-memory manipulation for Kotlin objects.

## Compatibility with:

Int

Double

Boolean

String

List< supported type >

Enums

null

data classes with properties whose type is supported

maps (Map) that associate Strings (keys) to any of the 
above Kotlin elements

## Getting Started

### Prerequisites

- Kotlin based

- Kotlin-reflect library

### Usage

The library, JsonLib.kt, can be imported and used in other projects.

To run a HTTP server that serves Json objects to specific end points, run the main() in GetJson.kt.

For custom end points, creat a controller class and map it and its functions with the @Mapping() notation and the path as its parameter

Example:

```kotlin
@Mapping("api")
class Controller {
    @Mapping("ints")
    fun demo(): List<Int> = listOf(1, 2, 3)
}
```

Then instanciate a GetJson object with the controller as a parameter and use .start(port) function to start the http server at port.

The example would return the Json converted "listOf(1, 2, 3)" at in the http get at http://localhost:8000/api/ints


## Running Tests

To test the HTTP server mentioned earlier, you can just use the tests already given.

These tests use an external library for performing HTTP requests, OkHttp.

Example:

```bash
# Command to run tests
 private val client = OkHttpClient()
 
    @Test
    fun testApiInts() {
        val request = Request.Builder()
            .url("http://localhost:8000/api/ints")
            .build()

        client.newCall(request).execute().use { response ->
            val body = response.body?.string() ?: ""
            println(body)
            assertEquals("[1, 2, 3]",body)
        }
    }
```

This example is the test for the example shown earlier in the chapter "Usage".

The example checks if in the url http://localhost:8000/api/ints, the body of the GET request response is [1, 2, 3].

## UML classes graph

![Untitled Diagram drawio](https://github.com/user-attachments/assets/ed43cd04-d92a-4b0a-b8a8-eda3305d6509)
