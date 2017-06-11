package functions

/**
 * Created by lwu on 6/11/17.
 */
infix fun String.shouldBeEqualTo(value: String) = this == value

fun main(args: Array<String>) {
    println("Hello".shouldBeEqualTo("Hello"))
    println("Hello" shouldBeEqualTo "Hello")
}