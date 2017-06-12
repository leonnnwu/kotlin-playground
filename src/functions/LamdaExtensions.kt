package functions

/**
 * Created by lwu on 6/11/17.
 */
class Request(val method: String, val query: String, val contentType: String)
data class Response(var contents: String, var status: Status) {
    fun status(f: Status.() -> Unit) {
        status.f()
    }
}

data class Status(var code: Int, var description: String)


class RouteHandler(val request: Request, val response: Response) {
    var executeNext = false
    fun next() {
        executeNext = true
    }

    fun response(f: Response.() -> Unit): Response {
        response.f()
        return response
    }
}


fun routeHandler(path: String, f: RouteHandler.() -> Response): Response {
    val routeHandlerObject = RouteHandler(Request("Get", "query", "contentType"), Response("", Status(200, "")))
    return routeHandlerObject.f()
}

fun main(args: Array<String>) {

    val responseObject = routeHandler("/index.html") {
        if (request.query != "") {
            //process
        }
        response {
            status {
                code = 404
                description = "Not found"
            }
        }
    }

    println(responseObject)
}