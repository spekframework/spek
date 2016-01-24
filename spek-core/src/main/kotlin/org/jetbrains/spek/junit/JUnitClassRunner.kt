package org.jetbrains.spek.junit

import org.jetbrains.spek.api.*
import org.junit.runner.Description
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunNotifier
import org.junit.runners.ParentRunner
import java.io.Serializable
import kotlin.collections.arrayListOf
import kotlin.collections.getOrPut
import kotlin.collections.hashMapOf

data class JUnitUniqueId(val id: Int) : Serializable {
    companion object {
        var id = 0
        fun next() = JUnitUniqueId(id++)
    }
}

public fun junitAction(description: Description, notifier: RunNotifier, action: () -> Unit) {
    if (description.isTest) notifier.fireTestStarted(description)
    try {
        action()
    } catch(e: SkippedException) {
        notifier.fireTestIgnored(description)
    } catch(e: PendingException) {
        notifier.fireTestIgnored(description)
    } catch(e: Throwable) {
        notifier.fireTestFailure(Failure(description, e))
    } finally {
        if (description.isTest) notifier.fireTestFinished(description)
    }
}

public class JUnitOnRunner<T>(val specificationClass: Class<T>, val given: TestGivenAction, val on: TestOnAction) : ParentRunner<TestItAction>(specificationClass) {

    val _children by lazy(LazyThreadSafetyMode.NONE) {
        val result = arrayListOf<TestItAction>()
        try {
            on.iterateIt { result.add(it) }
        } catch (e: SkippedException) {
        } catch (e: PendingException) {
        }
        result
    }

    val _description by lazy(LazyThreadSafetyMode.NONE) {
        val desc = Description.createSuiteDescription(on.description(), JUnitUniqueId.next())!!
        for (item in children) {
            desc.addChild(describeChild(item))
        }
        desc
    }

    val childrenDescriptions = hashMapOf<String, Description>()

    override fun getChildren(): MutableList<TestItAction> = _children
    override fun getDescription(): Description? = _description

    protected override fun describeChild(child: TestItAction?): Description? {
        return childrenDescriptions.getOrPut(child!!.description(), {
            Description.createSuiteDescription("${child.description()} (${on.description()})", JUnitUniqueId.next())!!
        })
    }

    protected override fun runChild(child: TestItAction?, notifier: RunNotifier?) {
        junitAction(describeChild(child)!!, notifier!!) {
            child!!.run()
        }
    }
}

public class JUnitGivenRunner<T>(val specificationClass: Class<T>, val given: TestGivenAction) : ParentRunner<JUnitOnRunner<T>>(specificationClass) {

    val _children by lazy(LazyThreadSafetyMode.NONE) {
        val result = arrayListOf<JUnitOnRunner<T>>()
        try {
            given.iterateOn { result.add(JUnitOnRunner(specificationClass, given, it)) }
        } catch (e: SkippedException) {
        } catch (e: PendingException) {
        }
        result
    }

    val _description by lazy(LazyThreadSafetyMode.NONE) {
        val desc = Description.createSuiteDescription(given.description(), JUnitUniqueId.next())!!
        for (item in children) {
            desc.addChild(describeChild(item))
        }
        desc
    }

    override fun getChildren(): MutableList<JUnitOnRunner<T>> = _children
    override fun getDescription(): Description? = _description

    protected override fun describeChild(child: JUnitOnRunner<T>?): Description? {
        return child?.description
    }

    protected override fun runChild(child: JUnitOnRunner<T>?, notifier: RunNotifier?) {
        junitAction(describeChild(child)!!, notifier!!) {
            child!!.run(notifier)
        }
    }
}

public class JUnitClassRunner<T>(val specificationClass: Class<T>) : ParentRunner<JUnitGivenRunner<T>>(specificationClass) {
    private val suiteDescription = Description.createSuiteDescription(specificationClass)

    override fun getChildren(): MutableList<JUnitGivenRunner<T>> = _children

    val _children by lazy(LazyThreadSafetyMode.NONE) {
        if (Spek::class.java.isAssignableFrom(specificationClass) && !specificationClass.isLocalClass) {
            val spek = specificationClass.newInstance() as Spek
            val result = arrayListOf<JUnitGivenRunner<T>>()
            spek.iterateGiven { result.add(JUnitGivenRunner(specificationClass, it)) }
            result
        } else
            arrayListOf()
    }

    protected override fun describeChild(child: JUnitGivenRunner<T>?): Description? {
        return child?.description
    }

    protected override fun runChild(child: JUnitGivenRunner<T>?, notifier: RunNotifier?) {
        junitAction(describeChild(child)!!, notifier!!) {
            child!!.run(notifier)
        }
    }
}
