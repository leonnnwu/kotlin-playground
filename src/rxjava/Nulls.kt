package rxjava

import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test
import java.lang.NullPointerException

class Nulls {

    @Test(expected = NullPointerException::class)
    fun testObservableJust() {
        Observable.just(null)
    }

    @Test(expected = NullPointerException::class)
    fun testSingleJust() {
        Single.just(null)
    }

    @Test
    fun testFromCallable() {
        Observable.fromCallable { null }.test().assertError(NullPointerException::class.java)
    }

    @Test
    fun testMapNull() {
//        Observable.just(1).map { null }.test().assertError(NullPointerException::class.java)
    }

    @Test
    fun testEmitter() {
        Observable.create<Any> {
            println("Side-effect 1")
            it.onNext(Irrelevant.INSTANCE)

            println("Side-effect 2")
            it.onNext(Irrelevant.INSTANCE)

            println("Side-effect 3")
            it.onNext(Irrelevant.INSTANCE)
        }
                .test()
                .assertNoErrors()

    }

    enum class Irrelevant { INSTANCE }
}