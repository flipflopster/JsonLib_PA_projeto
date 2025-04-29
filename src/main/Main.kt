package main

fun main() {
    println("Hello World!")
    val person = Person("John", 30)
    println(person.name) // Output: John
    println(person.age)  // Output: 30
    person.age = 31      // You can change the age because it's a var
    println(person.age)  // Output: 31
}

class Person(val name: String, var age: Int) {
    init {

        println("Person created with name: $name and age: $age")
    }
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