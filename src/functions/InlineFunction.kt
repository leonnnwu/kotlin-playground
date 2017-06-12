package functions

/**
 * Created by lwu on 6/11/17.
 */
inline fun operation(op: () -> Unit) {
    println("Before calling op()")
    op()
    throw Exception("A Message")
    println("After calling op()")
}

fun tryingToInline(op: () -> Unit) {
    val reference = op
    println("Assigned value")
    op()
}

fun anotherFunction() {
    operation { println("This is the actual op function.") }
}

fun main(args: Array<String>) {

    operation { println("This is the actual op function.") }

}