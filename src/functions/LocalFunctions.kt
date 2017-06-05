package functions

/**
 * Created by lwu on 6/4/17.
 */
fun foo(fooParam: String) {

    val outerFunction = "outer value"

    fun bar(barParam: String) {
        println(barParam)
        println(fooParam)
        println(outerFunction)
    }

    bar("bar value")
}

fun main(args: Array<String>) {
    foo("foo value")
}