package main
fun main() {
    println("Hello World!")
    val person = Person("John", 30)
    println(person.name) // Output: John
    println(person.age)  // Output: 30
    person.age = 31      // You can change the age because it's a var
    println(person.age)  // Output: 31
    val result =  "{\"name\": \"PA\", \"credits\": 6, \"evaluation\": [{\"name\": \"quizzes\", \"percentage\": 0.2, \"mandatory\": false, \"type\": null}, {\"name\": \"project\", \"percentage\": 0.8, \"mandatory\": true, \"type\": \"PROJECT\"}]}"
    println(result)
/*
    val lista = listOf(1,2,3)
    val teste = mapOf("teste" to 12, "ola" to "adeus", "lista" to lista)
    println(toJsonElement(teste))
 */
    val course = Course(
        "PA", 6, listOf(
            EvalItem("quizzes", .2, false, null),
            EvalItem("project", .8, true, EvalType.PROJECT)
        )
    )
    println(toJsonElement(course))
}


class Person(val name: String, var age: Int) {
    init {

        println("Person created with name: $name and age: $age")
    }
}

data class Course(
    val name: String,
    val credits: Int,
    val evaluation: List<EvalItem>
)


data class EvalItem(
    val name: String,
    val percentage: Double,
    val mandatory: Boolean,
    val type: EvalType?
)


enum class EvalType {
    TEST, PROJECT, EXAM
}



/*
fun accept(visitor: (jsonElement) -> Unit) {
    when (this) {

        is JsonObject -> {
            visitor(this)

            map.forEach{
                visitor(this)
            }

        }

        else -> visitor(this)
    }
}

fun accept(visitor: (Element) -> Unit) {
    when (this) {
        is FileElement -> visitor(this)
        is DirectoryElement -> {
            visitor(this)
            getChildren.forEach{
                it.accept(visitor)
            }
        }
    }
}

 */