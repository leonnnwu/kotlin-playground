package rxjava

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

/**
 * Created by lwu on 7/16/17.
 */

fun main(args: Array<String>) {
    val observable1 = Observable.create<Int> { emitter ->
        println("emit 1")
        emitter.onNext(1)
        Thread.sleep(1000)


        println("emit 2")
        emitter.onNext(2)
        Thread.sleep(1000)

        println("emit 3")
        emitter.onNext(3)
        Thread.sleep(1000)

        println("emit 4")
        emitter.onNext(4)
        Thread.sleep(1000)

        emitter.onComplete()
    }.doOnComplete {
        println("emit complete1")
    }.subscribeOn(Schedulers.io())

    val observable2 = Observable.create<String> { emitter ->
        println("emit A")
        emitter.onNext("A")
        Thread.sleep(1000)

        println("emit B")
        emitter.onNext("B")
        Thread.sleep(1000)

        println("emit C")
        emitter.onNext("C")
        Thread.sleep(1000)

        emitter.onComplete()
    }.doOnComplete {
        println("emit complete2")
    }.subscribeOn(Schedulers.io())

    val mainThread = Thread.currentThread()

    Observable
            .zip(observable1, observable2, BiFunction<Int, String, String> { t1, t2 ->
                "$t1$t2"
            })
            .subscribe(
                    { value ->
                        println("onNext $value")
                    },
                    {
                        println("onError")
                    },
                    {
                        println("onComplete")
                        mainThread.resume()
                    },
                    {
                        println("onSubscribe")
                    }
            )

    mainThread.suspend()
}