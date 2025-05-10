package main

import com.sun.net.httpserver.HttpServer
import junit.framework.TestCase.assertEquals
import main.Tests.Course
import main.Tests.EvalItem
import main.Tests.EvalType
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Test
import java.net.InetSocketAddress
import java.net.URI
import java.util.concurrent.Executors
import kotlin.reflect.*
import kotlin.reflect.full.*
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpExchange
import java.io.IOException
import java.io.OutputStream




fun main() {
    val app = GetJson(Controller::class)
    app.start(8000)
}

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Mapping(val value: String)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Param

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Path




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


    @Mapping("getJson")
    fun getTestObject() {

        val course = Course(
            "PA", 6, listOf(
                EvalItem("quizzes", .2, false, null),
                EvalItem("project", .8, true,
                    EvalType.PROJECT)
            )
        )

        val courseJson = toJsonElement(course).toString()



    }
    
}







class GetJson(vararg controllers: KClass<*>) {

    private val controllers: List<KClass<*>> = controllers.toList()

    private inner class TestHttp: HttpHandler {

        @Throws(IOException::class)
        override fun handle(exchange: HttpExchange) {

            val requestURI: URI = exchange.requestURI
            val path: String = requestURI.path

            val result = mapUrlToFunctionResult(path)


            if (exchange.requestMethod == "GET") {
                //print("Received a GET request")
                val response = "This is the response to a GET request"
                exchange.sendResponseHeaders(200, response.length.toLong())
                val os: OutputStream = exchange.responseBody
                os.write(response.toByteArray())
                os.close()
            } else {
                exchange.sendResponseHeaders(405, -1L) // 405 Method Not Allowed
            }
        }
    }

    fun start(port: Int){
        val server = HttpServer.create(InetSocketAddress(port), 0)
        server.createContext("/", TestHttp())
        server.start()
        println("Server started on port $port")
    }

    fun mapUrlToFunctionResult(path: String){

        val pathSeparated = path.replace("^/+".toRegex(), "").split("/")


        val con: List<KClass<*>> = mutableListOf<KClass<*>>()

        con.addAll(controllers.filter{ it.findAnnotation<Mapping>()?.value == pathSeparated[0] })

        val func: List<KFunction<*>> = mutableListOf<KFunction<*>>()
        
        if(pathSeparated.size > 1)
                con.forEach { func.addAll(it.declaredMemberFunctions.filter{ it.findAnnotation<Mapping>()?.value == pathSeparated[1] }) }


    }

}