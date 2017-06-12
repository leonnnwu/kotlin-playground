package functions

/**
 * Created by lwu on 6/11/17.
 */
fun containingFunction() {
    val numbers = 1 .. 100
    numbers.forEach myLabel@{
        if (it % 5 == 0) {
            return@myLabel
        }
    }

    numbers.forEach ( fun(element) {
        if (element % 5 == 0) {
            return
        }
    })
    println("Hello!")
}

fun main(args: Array<String>) {
    containingFunction()
}
