interface JsonElement{

    fun accept(visitor: (JsonElement) -> Unit) {
        when (this) {


            is JsonObject -> {
                visitor(this)
                getValues.forEach{
                    it.accept(visitor)
                }
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
}

class JsonObject (val map: MutableMap<String, JsonElement> = mutableMapOf<String, JsonElement>()) : JsonElement  {

    
    val getValues get() = map.values.toList()

    fun serializeToString() {

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

class JsonArray (val list: MutableList<JsonElement> = mutableListOf<JsonElement>()) : JsonElement {

    init {
        if (list.isNotEmpty()) {
            //TODO check se são todos do mesmo tipo usando accept()
        }
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
    }

    val getList: List<JsonElement> get() = list.toList()

    override fun toString(): String {
        var result : String = "["
        list.forEach {
            result += it.toString() + ", "
        }
        return result.dropLast(2) + "]"
    }


}

class JsonNumber (val integer: Int) : JsonElement {
    override fun toString(): String {
        return integer.toString()
    }
}

class JsonString(val string: String) : JsonElement {

    override fun toString(): String = "\"$string\""

}

class JsonBoolean (val boolean: Boolean) : JsonElement {
    override fun toString(): String = if (boolean) "true" else "false"
}

object JsonNull : JsonElement {
    override fun toString(): String = "null"
}