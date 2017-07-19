package rxjava

import io.reactivex.Maybe
import io.reactivex.Observable

/**
 * Created by lwu on 7/16/17.
 */
fun main(args: Array<String>) {
//    Maybe.just(1)
//            .map { v ->
//                v+1
//            }
//            .filter { v ->
//                v == 1
//            }
//            .subscribe({ onSuccess ->
//                println("Maybe Success: ${onSuccess}")
//            }, { onError ->
//                println("Maybe Error: ${onError.message}")
//            })
//
//    Observable.just(1)
//            .map { v ->
//                v+1
//            }
//            .filter { v ->
//                v == 1
//            }
//            .subscribe({ onNext ->
//                println("Observable Success: ${onNext}")
//            }, { onError ->
//                println("Observable Error: ${onError.message}")
//            })

    Observable.just(1, 2, 3)
            .doOnNext {
                println("Observable emit $it")
            }
            .flatMap {
                Observable.just(it+1)
            }
            .doOnNext {
                println("Observable flatMap emit $it")
            }
            .subscribe({ onNext ->
                println("Observable Next: ${onNext}")
            }, { onError ->
                println("Observable Error: ${onError.message}")
            })

//    Observable.just(1, 2, 3)
//            .doOnNext {
//                println("First emit $it")
//            }
//            .firstElement()
//            .subscribe({ onSuccess ->
//                println("First Success: ${onSuccess}")
//            }, { onError ->
//                println("First Error: ${onError.message}")
//            })
//
//    Observable.concat<Int>(Observable.empty(), Observable.empty(), Observable.empty())
//            .doOnNext {
//                println("Concat First emit $it")
//            }
//            .firstElement()
//            .subscribe({ onSuccess ->
//                println("Concat First Success: ${onSuccess}")
//            }, { onError ->
//                println("Concat First Error: ${onError.message}")
//            })
}