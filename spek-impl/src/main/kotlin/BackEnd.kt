package org.spek.impl

import org.spek.api.*
import kotlin.test.assertEquals
import kotlin.test.assertNot
import kotlin.test.assertNull
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

public trait TestFixtureAction {
    fun description(): String
    fun performInit(): List<TestGivenAction>
}

public trait TestGivenAction {
    fun description(): String
    fun performInit(): List<TestOnAction>
}

public trait TestOnAction {
    fun description(): String
    fun performInit(): List<TestItAction>
}

public trait TestItAction {
    fun description(): String
    fun run()
}

open public class SpekImpl: SpekWithDefaults, SkipSupportImpl() {
    private val recordedActions = arrayListOf<TestGivenAction>()

    ///workaround for KT-3628
    override fun given(description: String) {
        super<SpekWithDefaults>.given(description)
    }

    override fun given(description: String, givenExpression: Given.() -> Unit) {
        recordedActions.add(
                object : TestGivenAction {
                    public override fun description() = "given " + description
                    public override fun performInit(): List<TestOnAction> {
                        val given = GivenImpl()
                        given.givenExpression()
                        return given.getActions()
                    }
                })
    }

    public fun allGiven(): List<TestGivenAction> = recordedActions
}

public class GivenImpl: GivenWithDefaults, SkipSupportImpl() {
    private val recordedActions = arrayListOf<TestOnAction>()

    public fun getActions(): List<TestOnAction> = recordedActions

    public override fun on(description: String, onExpression: On.() -> Unit) {
        recordedActions.add(
                object : TestOnAction {
                    public override fun description() = "on " + description
                    public override fun performInit(): List<TestItAction> {
                        val on = OnImpl()
                        on.onExpression()
                        return on.getActions()
                    }
                })
    }
}

public class OnImpl: OnWithDefaults, SkipSupportImpl() {
    private val recordedActions = arrayListOf<TestItAction>()

    public fun getActions(): List<TestItAction> = recordedActions

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
