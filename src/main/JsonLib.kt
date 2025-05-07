package main

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

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

    /**
     * Valida se todos os JsonArray são válidos
     *
     * @return true caso todos os JsonArray são válidos, false caso contrário
     */
    fun validateArraysDepth(): Boolean{
        var valid: Boolean=true
        accept {
            if(it::class == JsonArray::class)
                if(!(it as JsonArray).checkSameTypeElements())
                    valid = false

        }
        return valid
    }

    /**
     * Valida se todos os JsonObject são válidos
     *
     * @return true caso todos os JsonObject são válidos, false caso contrário
     */
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

    /**
     * Tupple de um JsonString como chave e um JsonElement como o seu valor.
     *
     * @param key chave em JsonString do par
     * @param value da chave como JsonElement
     * @property key nome da variavel em json
     * @property value valor da variavel em json
     * @constructor cria um tuplo de json.
     */
data class JsonObjectTupple(val key: JsonString, val value: JsonElement) : JsonElement {

    override fun toString(): String {
        return key.toString() + ": " + value.toString()
    }
}

    /**
     * Objeto de um json.
     *
     * @param list lista de JsonObjectTupple
     * @property list lista de valores json do objeto
     * @constructor cria um objeto json.
     */
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

    /**
     * Filtro que devolve um novo JsonObject com base no predicate dado nu outro JsonObject.
     *
     * @param predicate que indica quais são os elementos do JsonObject que devem de ser incluídos
     * @return JsonObject com os elementos escolhidos pelo filtro.
     */
    fun filter(predicate: (JsonObjectTupple) -> Boolean): JsonObject {
        val newList = mutableListOf<JsonObjectTupple>()
        list.forEach {
            if (predicate(it)) {
                newList.add(it)
            }
        }
        return JsonObject(newList)
    }

    /**
     * Validador das chaves do JsonObject.
     *
     * @return true se todas as chaves do JsonObject forem válidas no formato json, false caso contrário
     */
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

    /**
     * Array de um json.
     *
     * @param list lista de JsonElement
     * @property list lista de valores json do array
     * @constructor cria um array json.
     */
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

    /**
     * Validador para verificar se elementos do JsonArray são do mesmo tipo.
     *
     * @return true se for tudo do mesmo tipo, false caso contrário.
     */
    fun checkSameTypeElements(): Boolean {
        val c = list.first()::class
        list.forEach{
            if (it::class!= c) {
                return false
            }
        }
        return true
    }

    /**
     * Filtro que devolve um novo JsonArray com base no predicate dado nu outro JsonObject.
     *
     * @param predicate que indica quais são os elementos do JsonArray que devem de ser incluídos
     * @return JsonArray com os elementos escolhidos pelo filtro.
     */
    fun filter(predicate: (JsonElement) -> Boolean): JsonArray {
        val newList = mutableListOf<JsonElement>()
        list.forEach {
            if (predicate(it)) {
                newList.add(it)
            }

        }
        return JsonArray(newList)
    }

    /**
     * Aplica uma transformação em cada um dos elementos do JsonArray, originando um novo JsonArray.
     *
     * @param transform que vai ser aplicada aos elementos
     * @return JsonArray com os novos elementos transformados
     */
    fun map(transform: (JsonElement) -> JsonElement): JsonArray {

        val newList = mutableListOf<JsonElement>()
        list.forEach {
            newList.add(transform(it))
        }
        return JsonArray(newList)
    }

    /**
     * Adiciona um novo elemento ao JsonArray.
     *
     * @param elemento a ser adicionado
     */
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

    /**
     * Número de um json.
     *
     * @param integer Number que queremos transformar em json
     * @property integer um numero em json.
     * @constructor cria um número json.
     */
data class JsonNumber (private val integer: Number = 0) : JsonElement {


    val value: Number get() = integer

    override fun toString(): String {
        return integer.toString()
    }

}

    /**
     * String de um json.
     *
     * @param string String que queremos transformar em json
     * @property string String em json
     * @constructor cria uma string json.
     */
data class JsonString(private val string: String) : JsonElement {

    val value: String get() = string

    override fun toString(): String = "\"$string\""

}

    /**
     * Enum de um boolean json
     *
     * @constructor cria um boolean json.
     */
enum class JsonBoolean : JsonElement {
    TRUE, FALSE;


    override fun toString(): String {
        return when (this) {
            TRUE -> "true"
            FALSE -> "false"
        }
    }



}

    /**
     * Objeto null json
     *
     * @constructor cria um null json.
     */
object JsonNull : JsonElement {
    override fun toString(): String = "null"
}

    /**
     * Json que provém de classes kotlin.
     *
     * @param element qualquer objeto de uma classe do kotlin
     * @property element  objeto que queremos transformar para json
     * @return element ja transformado em json.
     */
    fun toJsonElement(element: Any?): JsonElement{
        return when(element) {
            is Int -> JsonNumber(element as Number)
            is Double -> JsonNumber(element as Number)
            is Boolean -> if (element) JsonBoolean.TRUE else JsonBoolean.FALSE
            is String -> JsonString(element)
            is List<*> -> toJsonElementList(element)
            is Enum<*> -> JsonString(element.name)
            null -> JsonNull
            is Map<*,*> -> toJsonElementMap(element)

            else -> {
                val clazz = element::class
                val aux = mutableListOf<JsonObjectTupple>()
                clazz.primaryConstructor?.parameters?.forEach {
                    val prop = clazz.matchProperty(it)
                    aux.add(JsonObjectTupple(JsonString(prop.name), toJsonElement(prop.call(element))))
                }
                JsonObject(aux)
            }
        }
    }

    private fun KClass<*>.matchProperty(parameter: KParameter) : KProperty<*> {
        require(isData)
        return declaredMemberProperties.first { it.name == parameter.name }
    }

    private fun toJsonElementList(lista: List<*>): JsonElement{
        val listJsonElement = JsonArray()
        lista.forEach{
            listJsonElement.add(toJsonElement(it))
        }
        return listJsonElement
    }

    private fun toJsonElementMap(mapa: Map<*,*>): JsonElement {
        val jsonObject = mutableListOf<JsonObjectTupple>()
        mapa.forEach{ k,v ->
            if(k is String){
                jsonObject.add(JsonObjectTupple(JsonString(k), toJsonElement(v)))
            } else throw IllegalArgumentException("Key has to be String")
        }
        return JsonObject(jsonObject)
    }