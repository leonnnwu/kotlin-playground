package rxjava

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.awaitility.Awaitility.await
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.TimeUnit

class RxJavaTest {
    private val words = arrayListOf<String>(
            "the",
            "quick",
            "brown",
            "fox",
            "jumped",
            "over",
            "the",
            "lazy",
            "dog"
    )

    @Test
    fun testInSameThread() {
        val results = mutableListOf<String>()

        Observable.fromIterable(words)
                .zipWith(Observable.range(1, Int.MAX_VALUE), BiFunction<String, Int, String> { str, index ->
                    "%2d. %s".format(index, str)
                })
                .subscribe({ result ->
                    results.add(result)
                })

        assertThat(results, notNullValue());
        assertThat(results, hasSize<String>(9))
        assertThat(results, hasItem(" 4. fox"))
    }

    @Test
    fun testUsingTestObserver() {
        val testObserver = TestObserver<String>()

        Observable.fromIterable(words)
                .zipWith(Observable.range(1, Int.MAX_VALUE), BiFunction<String, Int, String> { str, index ->
                    "%2d. %s".format(index, str)
                })
                .subscribe(testObserver)

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(9)
        assertThat(testObserver.values(), hasItem(" 4. fox"))
    }

    @Test
    fun testFailure() {
        val testObserver = TestObserver<String>()
        val exception = RuntimeException("boom!")

        Observable.fromIterable(words)
                .zipWith(Observable.range(1, Int.MAX_VALUE), BiFunction<String, Int, String> { str, index ->
                    "%2d. %s".format(index, str)
                })
                .concatWith(Observable.error(exception))
                .subscribe(testObserver)

        testObserver.assertError(exception)
        testObserver.assertNotComplete()
    }

    @Test
    fun testUsingComputationScheduler_awaitility() {
        val testObserver = TestObserver<String>()

        Observable.fromIterable(words)
                .zipWith(Observable.range(1, Int.MAX_VALUE), BiFunction<String, Int, String> { str, index ->
                    "%2d. %s".format(index, str)
                })
                .subscribeOn(Schedulers.computation())
                .subscribe(testObserver)

        await().timeout(2, TimeUnit.SECONDS).until(testObserver::valueCount, equalTo(9))

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        assertThat(testObserver.values(), hasItem(" 4. fox"))
    }

    @Test
    fun testUsingBlockingCall() {
        val results = Observable.fromIterable(words)
                .zipWith(Observable.range(1, Int.MAX_VALUE), BiFunction<String, Int, String> { str, index ->
                    "%2d. %s".format(index, str)
                })
                .subscribeOn(Schedulers.computation())
                .blockingIterable()

        assertThat(results, notNullValue());
        assertThat(results, iterableWithSize(9));
        assertThat(results, hasItem(" 4. fox"));
    }

    @Test
    fun testUsingComputationScheduler() {
        val testObserver = TestObserver<String>()

        Observable.fromIterable(words)
                .zipWith(Observable.range(1, Int.MAX_VALUE), BiFunction<String, Int, String> { str, index ->
                    "%2d. %s".format(index, str)
                })
                .subscribeOn(Schedulers.computation())
                .subscribe(testObserver)

        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        assertThat(testObserver.values(), hasItem(" 4. fox"))
    }

    @Test
    fun testUsingRxJavaPluginsWithImmediateScheduler() {
        RxJavaPlugins.setComputationSchedulerHandler { scheduler -> Schedulers.trampoline() }

        val observer = TestObserver<String>()

        val observable = Observable.fromIterable(words)
                .zipWith(Observable.range(1, Int.MAX_VALUE), BiFunction<String, Int, String> { str, index ->
                    "%2d. %s".format(index, str)
                })

        try {
            observable.subscribeOn(Schedulers.computation())
                    .subscribe(observer)

            observer.assertComplete()
            observer.assertNoErrors()
            observer.assertValueCount(9)
            assertThat(observer.values(), hasItem(" 4. fox"))
        } finally {
            RxJavaPlugins.reset()
        }

    }

    @Test
    fun testUsingImmediateSchedulersRule() {
        val observer = TestObserver<String>()

        val observable = Observable.fromIterable(words)
                .zipWith(Observable.range(1, Int.MAX_VALUE), BiFunction<String, Int, String> { str, index ->
                    "%2d. %s".format(index, str)
                })

        observable.subscribeOn(Schedulers.computation()).subscribe(observer)

        observer.assertComplete()
        observer.assertNoErrors()
        observer.assertValueCount(9)
        assertThat(observer.values(), hasItem(" 4. fox"))
    }

    @Test
    fun testUsingTestScheduler() {
        // given:
        val scheduler = TestScheduler()
        val observer = TestObserver<String>()
        val tick: Observable<Long> = Observable.interval(1, TimeUnit.SECONDS, scheduler)

        Observable.fromIterable(words)
                .zipWith(tick, BiFunction<String, Long, String> { str, index ->
                    "%2d. %s".format(index, str)
                })
                .subscribeOn(scheduler).subscribe(observer)

        // expect:
        observer.assertNoValues()
        observer.assertNotComplete()

        // when:
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        // then:
        observer.assertNoErrors()
        observer.assertValueCount(1)
        observer.assertValues(" 0. the")

        // then:
        scheduler.advanceTimeTo(100, TimeUnit.SECONDS)
        observer.assertComplete()
        observer.assertNoErrors()
        observer.assertValueCount(9)
    }

    @Test
    fun testUsingTestSchedulerRule() {
        // given:
        val observer = TestObserver<String>()

        Observable.fromIterable(words)
                .zipWith(Observable.interval(1, TimeUnit.SECONDS), BiFunction<String, Long, String> { str, index ->
                    "%2d. %s".format(index, str)
                })
                .subscribeOn(Schedulers.computation())
                .subscribe(observer)

        // expect
        observer.assertNoValues()
        observer.assertNotComplete()

        // when:
        testSchedulerRule.testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        // then:
        observer.assertNoErrors()
        observer.assertValueCount(1)
        observer.assertValues(" 0. the")

        // then:
        testSchedulerRule.testScheduler.advanceTimeTo(100, TimeUnit.SECONDS)
        observer.assertComplete()
        observer.assertNoErrors()
        observer.assertValueCount(9)
    }


//    @Rule
//    @JvmField val customSchedulers = ImmediateSchedulerRule()

    @Rule
    @JvmField val testSchedulerRule = TestSchedulerRule()

    class ImmediateSchedulerRule: TestRule {
        override fun apply(base: Statement, p1: Description?): Statement {
            return object : Statement() {
                override fun evaluate() {
                    RxJavaPlugins.setIoSchedulerHandler { scheduler -> Schedulers.trampoline() }
                    RxJavaPlugins.setComputationSchedulerHandler { scheduler -> Schedulers.trampoline() }
                    RxJavaPlugins.setNewThreadSchedulerHandler { scheduler -> Schedulers.trampoline() }

                    try {
                        base.evaluate()
                    } finally {
                        RxJavaPlugins.reset()
                    }
                }
            }

        }

    }

    class TestSchedulerRule: TestRule {

        val testScheduler = TestScheduler()

        override fun apply(base: Statement, p1: Description?): Statement {
            return object : Statement() {
                override fun evaluate() {
                    RxJavaPlugins.setIoSchedulerHandler { scheduler -> testScheduler }
                    RxJavaPlugins.setComputationSchedulerHandler { scheduler -> testScheduler }
                    RxJavaPlugins.setNewThreadSchedulerHandler { scheduler -> testScheduler }

                    try {
                        base.evaluate()
                    } finally {
                        RxJavaPlugins.reset()
                    }
                }
            }

        }

    }

}
