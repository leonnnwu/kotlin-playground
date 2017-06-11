package functions

/**
 * Created by lwu on 6/11/17.
 */

fun op(x: Int, op: (Int) -> Int): Int {
    return op(x)
}

fun main(args: Array<String>) {

    println(op(3, { it * it }))
    println(op(3, fun(x): Int { return  x * x} )) //Anonymous function
}