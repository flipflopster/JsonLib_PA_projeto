package main

import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import java.net.URI
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

    /**
     * Endpoint que devolve uma lista estática de inteiros.
     *
     */
    @Mapping("ints")
    fun demo(): List<Int> = listOf(1, 2, 3)

    /**
     * Endpoint que devolve um par de strings.
     *
     *
     */
    @Mapping("pair")
    fun pair(): Pair<String, String> = Pair("um", "dois")

    /**
     * Endpoint que devolve o valor recebido na URL com um ponto de exclamação.
     *
     * @param pathvar String a ser adicionada o !
     */
    @Mapping("path/{pathvar}")
    fun path(
        @Path pathvar: String
    ): String = pathvar + "!"

    /**
     * Endpoint que recebe dois parâmetros: um número `n` e um texto `text`.
     * Repete o texto `n` vezes e devolve um mapa onde a chave e o valor são o texto repetido.
     *
     * @param n número inteiro de quantas vezes a ser repetido
     * @param text String a ser repetida
     */
    @Mapping("args")
    fun args(
        @Param n: Int,
        @Param text: String
    ): Map<String, String> = mapOf(text to text.repeat(n))

    
}

class GetJson(vararg controllers: KClass<*>) {

    private val ctrObjs: List<Any> = controllers.map { it.createInstance() }


    private inner class TestHttp: HttpHandler {

        @Throws(IOException::class)
        override fun handle(exchange: HttpExchange) {

            val requestURI: URI = exchange.requestURI

            //val path: String = requestURI.path

            val result = mapUrlToFunctionResult(requestURI)
            val resultJson = toJsonElement(result).toString()


            if (exchange.requestMethod == "GET") {
                //print("Received a GET request")
                val response = resultJson
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

    fun mapUrlToFunctionResult(uri: URI): Any? {


        val pathSeparated = uri.path.replace("^/+".toRegex(), "").split("/")





        val con = mutableListOf<Any>()

        ctrObjs.filter { it::class.findAnnotation<Mapping>()?.value == pathSeparated[0] }.forEach { con.add(it) }

        val func = mutableListOf<KFunction<*>>()

        if (pathSeparated.size > 1)
            con.forEach {
                it::class.declaredMemberFunctions.filter {
                    it.findAnnotation<Mapping>()?.value?.replace(
                        "^/+".toRegex(),
                        ""
                    )?.split("/")?.first() == pathSeparated[1]
                }.forEach { func.add(it) }
            }


        //println(func[0].call(con[0])) // dar como argumento o objeto controller porque naa pode ser static
        //println(func[0])


        when (pathSeparated[1]) {
            "path" -> {
                val params = func[0].parameters.map { it.findAnnotation<Path>() }
                println(func[0].call(con[0], pathSeparated[2]))
                return func[0].call(con[0], pathSeparated[2])
            }

            "args" -> {
                val params = func[0].parameters.map { it.findAnnotation<Param>() }
                //println(pathSeparated[1].split("?")[1])
                val queryParams = uri.query.split("&").map { it.split("=") }
                return func[0].call(con[0], queryParams[0][1].toInt(), queryParams[1][1])
            }

            else -> {
                return func[0].call(con[0])
            }


        }
        return "Function not found"
    }

}