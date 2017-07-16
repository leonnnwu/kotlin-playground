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
        var i = 0
        while (true) {
            emitter.onNext(i++)
        }
    }, BackpressureStrategy.LATEST)
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
        subscription?.request(128)
    }

}