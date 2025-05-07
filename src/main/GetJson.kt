package main

import com.sun.net.httpserver.HttpServer
import junit.framework.TestCase.assertEquals
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Test
import java.net.InetSocketAddress
import java.net.URI
import java.util.concurrent.Executors
import kotlin.reflect.*
import kotlin.reflect.full.*


fun main() {
    val app = GetJson(Controller::class)
    app.start(8080)
}

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Mapping(val value: String)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Param

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Path

annotation class Body


@Mapping("api")
class Controller {

    @Mapping("ints")
    fun demo(): List<Int> = listOf(1, 2, 3)

    @Mapping("pair")
    fun obj(): Pair<String, String> = Pair("um", "dois")

    @Mapping("path/{pathvar}")
    fun path(
        @Path pathvar: String
    ): String = pathvar + "!"

    @Mapping("args")
    fun args(
        @Param n: Int,
        @Param text: String
    ): Map<String, String> = mapOf(text to text.repeat(n))

    @Mapping("kotlinToJson")
    fun kotlinToJson(
        @Param obj: Any
    ): JsonElement {
        return toJsonElement(obj)
    }
    
}




class GetJson(vararg controllers: KClass<*>) {

}