package main

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Tests {

    val jsonObj = JsonObject()
    val jsonString = JsonString("Hello, World!")
    val jsonNumber = JsonNumber(42)
    val jsonBoolean = JsonBoolean.TRUE
    val jsonArray = JsonArray(mutableListOf(JsonString("item1"), JsonString("item2")))
    val nestedJsonObject = JsonObject(mutableListOf(JsonObjectTupple(JsonString("key1"), JsonString("nestedValue"))))




    val exampleObject = JsonObject(
        mutableListOf(JsonObjectTupple(JsonString("string"), jsonString),
                    JsonObjectTupple(JsonString("number"), jsonNumber),
                    JsonObjectTupple(JsonString("boolean"), jsonBoolean),
                    JsonObjectTupple(JsonString("array"), jsonArray),

                    JsonObjectTupple(JsonString("nestedObject"), nestedJsonObject)

        )

    )

    val exampleObject2 = JsonObject(
        mutableListOf(

            JsonObjectTupple(JsonString("number"), jsonNumber)
        )
    )

    @Test(expected = IllegalArgumentException::class)
    fun testJsonArrayError() {
        val jsonArrayFalse = JsonArray(mutableListOf(jsonNumber,jsonString))

    }

    @Test
    fun testJsonArrayObjects() {
        val jsonArrayFalse2 = JsonArray(mutableListOf(exampleObject,exampleObject2))
        val jsonArrayFalse3 = JsonArray(mutableListOf(exampleObject,exampleObject))
        val jsonArrayFalse4 = JsonArray(mutableListOf(exampleObject,exampleObject2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testJsonArrayErrorAdd() {
        val jsonArrayFalse = JsonArray(mutableListOf(jsonNumber))
        jsonArrayFalse.add(jsonString)
    }

    @Test
    fun testJsonObjectFilter() {

        val filteredObject = exampleObject.filter { it.value::class == JsonString::class }

        assertEquals(JsonObject(mutableListOf(JsonObjectTupple(JsonString("string"), jsonString))), filteredObject)

    }

    @Test
    fun testJsonArrayFilter() {

        val filteredArray = jsonArray.filter { it == JsonString("item1") }
        assertEquals(JsonArray(mutableListOf(JsonString("item1"))), filteredArray)
    }


    @Test
    fun testJsonArrayMap() {

        val originalArray = JsonArray(mutableListOf(JsonString("string"), JsonString("ola")))
        val mappedArray = originalArray.map {
            if (it is JsonString)
                JsonString(it.value.uppercase())
            else it
        }
        val teste = JsonArray(mutableListOf(JsonString("STRING"), JsonString("OLA")))
        assertEquals(teste,mappedArray)

    }


 @Test
    fun testSerializeJsonToString() {
        val str = "{\"string\": \"Hello, World!\", \"number\": 42, \"boolean\": true, \"array\": [\"item1\", \"item2\"], \"nestedObject\": {\"key1\": \"nestedValue\"}}"
        assertEquals(str, exampleObject.serializeToString())
    }



}
