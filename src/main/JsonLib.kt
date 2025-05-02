package main

interface JsonElement{



    fun accept(visitor: (JsonElement) -> Unit) {
        when (this) {


            is JsonObject -> {
                visitor(this)
                getList.forEach{
                    it.accept(visitor)
                }

            }

            is JsonObjectTupple -> {
                visitor(key)
                visitor(value)
            }

            is JsonArray -> {

                visitor(this)

                getList.forEach{
                    it.accept(visitor)
                }

            }

            else -> visitor(this)

        }
    }

    fun validateArraysDepth(): Boolean{
        var valid: Boolean=true
        accept {
            if(it::class == JsonArray::class)
                if(!(it as JsonArray).checkSameTypeElements())
                    valid = false

        }
        return valid
    }

    fun validateObjectsDepth(): Boolean{
        var valid: Boolean=true
        accept {
            if(it::class == JsonObject::class)
                if(!(it as JsonObject).checkValidKeys())
                    valid = false
        }
        return valid
    }



}

data class JsonObjectTupple(val key: JsonString, val value: JsonElement) : JsonElement {

    override fun toString(): String {
        return key.toString() + ": " + value.toString()
    }
}


data class JsonObject (val list: MutableList<JsonObjectTupple> = mutableListOf<JsonObjectTupple>()) : JsonElement {


    val getList get() = list.toList()

    fun serializeToString(): String {
        return toString()
    }

    override fun toString(): String {
        var result : String = "{"
        list.forEach{
            result = result +  it.toString() + ", "
        }
        return result.dropLast(2) + "}"
    }

    init {

    }

    fun filter(predicate: (JsonObjectTupple) -> Boolean): JsonObject {
        val newList = mutableListOf<JsonObjectTupple>()
        list.forEach {
            if (predicate(it)) {
                newList.add(it)
            }
        }
        return JsonObject(newList)
    }

    fun checkValidKeys(): Boolean {
        val keys: MutableList<JsonString> = mutableListOf()

        val controlCharactersRegex = "[\\x00-\\x1F\\x7F-\\x9F]".toRegex()
        val unescapedBackslashRegex = "\\\\[^\"]".toRegex()
        val doubleQuoteRegex = "\"".toRegex()

        list.forEach {

            if( it.key in keys){
                return false
            }

            keys.add(it.key)

            val str = it.key.value
            // Check for control characters
            if (controlCharactersRegex.containsMatchIn(str) || unescapedBackslashRegex.containsMatchIn(str) || doubleQuoteRegex.containsMatchIn(str)) {
                return false
            }


        }

        return true
    }



}

data class JsonArray (val list: MutableList<JsonElement> = mutableListOf<JsonElement>()) : JsonElement {

    init {
        /*
        if (list.isNotEmpty()) {
            val c = list.first()::class

            if (c == JsonNull::class) {
                throw IllegalArgumentException("The array cannot contain null values")
            }
            if(!checkSameTypeElements())
                throw IllegalArgumentException("All elements in the array must be of the same type")
        }
        */
        print("")
    }



    fun checkSameTypeElements(): Boolean {
        val c = list.first()::class
        list.forEach{
            if (it::class!= c) {
                return false
            }
        }
        return true
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

        list.forEach{
            result = result +  it.toString() + ", "
        }

        return result.dropLast(2) + "]"
    }


}

data class JsonNumber (private val integer: Int = 0) : JsonElement {


    val value: Int get() = integer

    override fun toString(): String {
        return integer.toString()
    }

}

data class JsonString(private val string: String) : JsonElement {

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