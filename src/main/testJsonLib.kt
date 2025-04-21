package main

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Tests {

    val jsonObj = JsonObject()
    val jsonString = JsonString("Hello, World!")
    val jsonNumber = JsonNumber(42)
    val jsonBoolean = JsonBoolean.TRUE
    val jsonArray = JsonArray(mutableListOf(JsonString("item1"), JsonString("item2")))
    val nestedJsonObject = JsonObject(mutableMapOf("key1" to JsonString("nestedValue")))

    val exampleObject = JsonObject(mutableMapOf(
        "string" to jsonString,
        "number" to jsonNumber,
        "boolean" to jsonBoolean,
        "array" to jsonArray,
        "nestedObject" to nestedJsonObject
    ))

    @Test
    fun testJsonArray() {

    }

    @Test
    fun testJsonFilter() {

        val filteredObject = exampleObject.filter { it::class == JsonString::class }
        assertEquals(JsonObject(mutableMapOf("string" to jsonString)),filteredObject)

    }

    @Test
    fun testJsonMap() {

        val mappedArray = jsonArray.map { it.value.upperCase() }

        }


    }

}