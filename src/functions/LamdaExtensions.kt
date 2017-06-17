package functions

/**
 * Created by lwu on 6/11/17.
 */
class Request(val method: String, val query: String, val contentType: String)
data class Response(var contents: String, var status: Status) {
    operator fun invoke(status: Status.() -> Unit) {

    }
}

data class Status(var code: Int, var description: String)


class RouteHandler(val request: Request, val response: Response) {
    var executeNext = false
    fun next() {
        executeNext = true
    }
}


fun routeHandler(path: String, f: RouteHandler.() -> Unit): RouteHandler.() -> Unit = f

fun main(args: Array<String>) {

    val responseObject = routeHandler("/index.html") {
        if (request.query != "") {
            //process
        }
        response {
            code = 404
            description = "Not found"
        }
    }

    println(responseObject)

    val manager = Manager()
    manager("Do Something")
}

class Manager {
    operator fun invoke(value: String) {

    }
}