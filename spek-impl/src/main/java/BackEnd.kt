package org.spek.impl

import org.spek.api.*
import kotlin.test.assertEquals
import kotlin.test.assertNot
import kotlin.test.assertNull
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

public trait TestFixtureAction {
    fun description(): String
    fun iterateGiven(it:(TestGivenAction) -> Unit)
}

public trait TestGivenAction {
    fun description(): String
    fun iterateOn(it: (TestOnAction) -> Unit)
}

public trait TestOnAction {
    fun description(): String
    fun iterateIt(it : (TestItAction) -> Unit)
}

public trait TestItAction {
    fun description(): String
    fun run()
}

open public class SpekImpl: SpekWithDefaults, SkipSupportImpl() {
    private val recordedActions = linkedListOf<TestGivenAction>()

    ///workaround for KT-3628
    override fun given(description: String) {
        super<SpekWithDefaults>.given(description)
    }

    override fun given(description: String, givenExpression: Given.() -> Unit) {
        recordedActions.add(
                object : TestGivenAction {
                    public override fun description() = "given " + description

                    public override fun iterateOn(it: (TestOnAction) -> Unit) {
                        val given = GivenImpl()
                        given.givenExpression()
                        given.iterateOn(it)
                    }
                })
    }

    public fun iterateGiven(it:(TestGivenAction) -> Unit) : Unit = removingIterator(recordedActions, it)

    public fun allGiven(): List<TestGivenAction> = recordedActions
}

public class GivenImpl: GivenWithDefaults, SkipSupportImpl() {
    private val recordedActions = linkedListOf<TestOnAction>()
    private val beforeActions = linkedListOf<()->Unit>()
    private val afterActions = linkedListOf<()->Unit>()

    public fun iterateOn(callback : (TestOnAction) -> Unit) : Unit = removingIterator(recordedActions) {
        beforeActions forEach { it() }
        try {
            callback(it)
        } finally {
            afterActions forEach { it() }
        }
    }

    override fun beforeOn(it: () -> Unit) {
        beforeActions add it
    }

    override fun afterOn(it: () -> Unit) {
        afterActions add it
    }

    public override fun on(description: String, onExpression: On.() -> Unit) {
        recordedActions.add(
                object : TestOnAction {
                    public override fun description() = "on " + description
                    public override fun iterateIt(it: (TestItAction) -> Unit) {
                        val on = OnImpl()
                        on.onExpression()
                        return on.iterateIt(it)
                    }
                })
    }
}

public class OnImpl: OnWithDefaults, SkipSupportImpl() {
    private val recordedActions = linkedListOf<TestItAction>()

    public fun iterateIt(it : (TestItAction) -> Unit) : Unit = removingIterator(recordedActions, it)

    public override fun it(description: String, itExpression: It.()->Unit) {
        recordedActions.add(
                object : TestItAction {
                    public override fun description() = "it " + description
                    public override fun run() = ItImpl().itExpression()
                })
    }
}

open class SkipSupportImpl: SkipSupportWithDefaults {

    override fun skip(why: String) = throw SkippedException(why)

    override fun pending(why: String) = throw PendingException(why)
}

open class ItImpl : It {
    override fun shouldEqual<T>(expected: T, actual: T) {
        assertEquals(expected, actual)
    }
    override fun shouldNotEqual<T>(expected: T, actual: T) {
        assertNot { expected == actual }
    }
    override fun shouldBeNull<T>(actual: T) {
        assertNull(actual)
    }
    override fun shouldNotBeNull<T>(actual: T) {
        assertNotNull(actual)
    }
    override fun shouldBeTrue<T>(actual: T) {
        assertTrue(actual == true)
    }
    override fun shouldBeFalse<T>(actual: T) {
        assertTrue(actual == false)
    }
}

class SkippedException(message: String): RuntimeException(message)

class PendingException(message: String): RuntimeException(message)
