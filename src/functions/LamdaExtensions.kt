package functions

/**
 * Created by lwu on 6/11/17.
 */
class Request(val method: String, val query: String, val contentType: String)
class Response(var contents: String, var status: Status) {
    fun statusHandler(status: Status.() -> Unit) {}
}

class Status(var code: Int, var description: String)


class RouteHandler(val request: Request, val response: Response) {
    var executeNext = false
    fun next() {
        executeNext = true
    }
}

fun responseHanlder(response: Response.() -> Unit) {}
fun routeHandler(path: String, f: RouteHandler.() -> Unit): RouteHandler.() -> Unit = f

fun main(args: Array<String>) {

    routeHandler("/index.html") {
        if (request.query != "") {
            //process
        }
        responseHanlder {
            statusHandler {
                code = 404
                description = "Not found"
            }
        }
    }
}