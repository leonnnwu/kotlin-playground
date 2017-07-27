package rxjava

import io.reactivex.Maybe
import io.reactivex.Observable
import org.junit.Test
import java.lang.IllegalArgumentException

class RetryTest {

    @Test
    fun testRetryMaybe() {

        val resourceObservable = Observable.just(Pair(1, 2))
                .doOnNext {
                    println("0.Emmit: $it")
                }
                .doOnError {
                    println("0.Error: ${it.message}")
                }
                .doOnComplete {
                    println("0.onComplete")
                }

        val maybe1 = resourceObservable
//                .flatMap { Observable.error<IllegalArgumentException>(IllegalArgumentException("Error Exception")) }
                .firstElement()
                .flatMap {
                    if (it.first != null) {
                        Maybe.just(it.first)
                    } else {
                        Maybe.empty()
                    }
                }
                .doOnSuccess {
                    println("1.Success: $it")
                }
                .doOnError {
                    println("1.Error: ${it.message}")
                }
                .doOnComplete {
                    println("1.onComplete")
                }
                .retry(3)

        val maybe2 = resourceObservable
                .firstElement()
                .flatMap {
                    if (it.first == null && it.second != null) {
                        Maybe.just(it.second)
                    } else {
                        Maybe.empty()
                    }
                }
                .doOnSuccess {
                    println("2.Success: $it")
                }
                .doOnError {
                    println("2.Error: ${it.message}")
                }
                .doOnComplete {
                    println("2.onComplete")
                }
                .retry(3)


        val maybe3 = resourceObservable
                .firstElement()
                .flatMap {
                    if (it.first == null && it.second == null) {
                        Maybe.just(3)
                    } else {
                        Maybe.empty()
                    }
                }
                .doOnSuccess {
                    println("3.Success: $it")
                }
                .doOnError {
                    println("3.Error: ${it.message}")
                }
                .doOnComplete {
                    println("4.onComplete")
                }
                .retry(3)

        Maybe.concat(maybe1, maybe2, maybe3)
                .firstElement()
                .doOnSuccess {
                    println("Final .Success: $it")
                }
                .doOnError {
                    println("Final .Error: ${it.message}")
                }
                .doOnComplete {
                    println("Final .onComplete")
                }
                .test()
                .assertComplete()

    }

    @Test
    fun testRetryObservable() {

        val resourceObservable = Observable.just(Pair(1, 2))
                .doOnNext {
                    println("0.Emmit: $it")
                }
                .doOnError {
                    println("0.Error: ${it.message}")
                }
                .doOnComplete {
                    println("0.onComplete")
                }

        val observabl1 = resourceObservable
//                .flatMap { Observable.error<IllegalArgumentException>(IllegalArgumentException("Error Exception")) }
                .flatMap {
                    if (it.first != null) {
                        Observable.just(it.first)
                    } else {
                        Observable.empty()
                    }
                }
                .doOnNext {
                    println("1.Next: $it")
                }
                .doOnError {
                    println("1.Error: ${it.message}")
                }
                .doOnComplete {
                    println("1.onComplete")
                }
                .retry(3)

        val observable2 = resourceObservable
                .flatMap {
                    if (it.first == null && it.second != null) {
                        Observable.just(it.second)
                    } else {
                        Observable.empty()
                    }
                }
                .doOnNext {
                    println("2.Next: $it")
                }
                .doOnError {
                    println("2.Error: ${it.message}")
                }
                .doOnComplete {
                    println("2.onComplete")
                }
                .retry(3)


        val observable3 = resourceObservable
                .flatMap {
                    if (it.first == null && it.second == null) {
                        Observable.just(3)
                    } else {
                        Observable.empty()
                    }
                }
                .doOnNext {
                    println("3.Next: $it")
                }
                .doOnError {
                    println("3.Error: ${it.message}")
                }
                .doOnComplete {
                    println("4.onComplete")
                }
                .retry(3)

        Observable.concat(observabl1, observable2, observable3)
                .firstElement()
                .doOnSuccess {
                    println("Final .Success: $it")
                }
                .doOnError {
                    println("Final .Error: ${it.message}")
                }
                .doOnComplete {
                    println("Final .onComplete")
                }
                .test()
                .assertComplete()

    }

}