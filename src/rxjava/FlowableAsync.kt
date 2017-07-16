package rxjava

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

/**
 * Created by lwu on 7/16/17.
 */
fun main(args: Array<String>) {
    var subscription: Subscription? = null

    Flowable.create<Int>({ emitter ->
        println("emit 1")
        emitter.onNext(1)

        println("emit 2")
        emitter.onNext(2)

        println("emit 3")
        emitter.onNext(3)

        println("emit complete")
        emitter.onComplete()
    }, BackpressureStrategy.ERROR)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe({
                println("onNext $it")
            }, {
                println("onError ${it.stackTrace}")
            }, {
                println("onComplete")
            }, {
                println("onSubscribe")
                subscription = it
            })

    (1L..3L).forEach {
        Thread.sleep(1000)
        subscription?.request(1)
    }
}