
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

