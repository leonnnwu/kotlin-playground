package rxjava

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

/**
 * Created by lwu on 7/16/17.
 */
fun main(args: Array<String>) {
    var subscription: Subscription? = null
    val upstream = Flowable.create<Int>({ emitter ->
        println("emit 1")
        emitter.onNext(1)

        println("emit 2")
        emitter.onNext(2)

        println("emit 3")
        emitter.onNext(3)

        println("emit complete")
        emitter.onComplete()
    }, BackpressureStrategy.ERROR)

    val downstream = object : Subscriber<Int> {
        override fun onComplete() {
            println("onComplete")
        }

        override fun onSubscribe(p0: Subscription) {
            println("onSubscribe")
            subscription = p0
            p0.request(1)
        }

        override fun onNext(p0: Int?) {
            println("onNext")
            subscription?.request(1)
        }

        override fun onError(p0: Throwable?) {
            println("onError")
        }
    }

    upstream.subscribe(downstream)
}