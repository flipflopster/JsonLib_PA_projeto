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

```bash
# Command to run tests
```
