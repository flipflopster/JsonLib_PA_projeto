package main

interface JsonElement{



    fun accept(visitor: (JsonElement) -> Unit) {
        when (this) {


            is JsonObject -> {
                visitor(this)
                getValues.forEach{
                    it.accept(visitor)
                }

            }

            is JsonObjectTupple -> {

            }

            is JsonArray -> {

                visitor(this) //TODO aplicar á própria lista

                getList.forEach{
                    it.accept(visitor)
                }

            }

            else -> visitor(this)

        }
    }
}

class JsonObjectTupple(val key: String, val value: JsonElement) : JsonElement

data class JsonObject (val map: MutableMap<String, JsonElement> = mutableMapOf<String, JsonElement>()) : JsonElement {


    val getValues get() = map.values.toList()

    fun serializeToString() {
        val result = ""
        accept {

        }
    }



    init {

    }

    fun filter(predicate: (JsonElement) -> Boolean): JsonObject {
        val newMap = mutableMapOf<String, JsonElement>()
        map.forEach {
            if (predicate(it.value)) {
                newMap[it.key] = it.value
            }

        }
        return JsonObject(newMap)
    }




}

data class JsonArray (val list: MutableList<JsonElement> = mutableListOf<JsonElement>()) : JsonElement {

    init {
        if (list.isNotEmpty()) {
            val c = list.first()::class

            if (c == JsonNull::class) {
                throw IllegalArgumentException("The array cannot contain null values")
            }
            if(!checkSameTypeElements())
                throw IllegalArgumentException("All elements in the array must be of the same type")
        }
    }

    fun checkSameTypeElements(): Boolean  { // is this correct??
        val c = list.first()::class
        var isValid = true
        accept {
            if (it in list && it::class!= c) {
                isValid = false
            }
        }
        return isValid
    }


    fun filter(predicate: (JsonElement) -> Boolean): JsonArray {
        val newList = mutableListOf<JsonElement>()
        list.forEach {
            if (predicate(it)) {
                newList.add(it)
            }

        }
        return JsonArray(newList)
    }

    fun map(transform: (JsonElement) -> JsonElement): JsonArray {

        val newList = mutableListOf<JsonElement>()
        list.forEach {
            newList.add(transform(it))
        }
        return JsonArray(newList)
    }

    fun add(element: JsonElement) {
        //TODO check se são todos do mesmo tipo
        list.add(element)
        if(!checkSameTypeElements())
            throw IllegalArgumentException("The added element in the array must be of the same type as the array")
    }

    val getList: List<JsonElement> get() = list.toList()

    override fun toString(): String {
        var result : String = "["

        return result.dropLast(2) + "]"
    }


}

data class JsonNumber (val integer: Int = 0) : JsonElement {

    override fun toString(): String {
        return integer.toString()
    }

}

data class JsonString(val string: String) : JsonElement {

    val value: String get() = string

    override fun toString(): String = "\"$string\""

}

enum class JsonBoolean : JsonElement {
    TRUE, FALSE;


    override fun toString(): String {
        return when (this) {
            TRUE -> "true"
            FALSE -> "false"
        }
    }



}



object JsonNull : JsonElement {
    override fun toString(): String = "null"
}