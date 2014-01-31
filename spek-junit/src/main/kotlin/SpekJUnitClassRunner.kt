package org.spek.junit.impl

import org.junit.runner.*
import org.junit.runners.*
import org.junit.runner.notification.*
import org.spek.impl.*
import org.spek.api.*
import kotlin.properties.Delegates
import java.io.Serializable

data class SpekUniqueId(val id : Int) : Serializable {
    class object {
        var id = 0
        fun next() = SpekUniqueId(id++)
    }
}

public fun testAction(description: Description, notifier: RunNotifier, action: () -> Unit) {
    notifier.fireTestStarted(description)
    try {
        action()
    } catch(e: SkippedException) {
        notifier.fireTestIgnored(description)
    } catch(e: PendingException) {
    } catch(e: Throwable) {
        notifier.fireTestFailure(Failure(description, e))
    } finally {
        notifier.fireTestFinished(description)
    }
}

public class SpekJUnitOnRunner<T>(val specificationClass: Class<T>, val given: TestGivenAction, val on: TestOnAction) : ParentRunner<TestItAction>(specificationClass) {

    val _children by Delegates.lazy {
        val result = arrayListOf<TestItAction>()
        on.iterateIt { result.add(it) }
        result
    }

    val _description by Delegates.lazy {
        val desc = Description.createSuiteDescription(on.description(), SpekUniqueId.next())!!
        for (item in getChildren()) {
            desc.addChild(describeChild(item))
        }
        desc
    }

    val childrenDescriptions = hashMapOf<String, Description>()

    override fun getChildren(): MutableList<TestItAction> = _children
    override fun getDescription(): Description? = _description

    protected override fun describeChild(child: TestItAction?): Description? {
        return childrenDescriptions.getOrPut(child!!.description(), {
            Description.createSuiteDescription(child.description(), SpekUniqueId.next())!!
        })
    }

    protected override fun runChild(child: TestItAction?, notifier: RunNotifier?) {
        testAction(describeChild(child)!!, notifier!!) {
            child!!.run()
        }
    }
}

public class SpekJUnitGivenRunner<T>(val specificationClass: Class<T>, val given: TestGivenAction) : ParentRunner<SpekJUnitOnRunner<T>>(specificationClass) {

    val _children by Delegates.lazy {
        val result = arrayListOf<SpekJUnitOnRunner<T>>()
        given.iterateOn { result.add(SpekJUnitOnRunner(specificationClass, given, it)) }
        result
    }

    val _description by Delegates.lazy {
        val desc = Description.createSuiteDescription(given.description(), SpekUniqueId.next())!!
        for (item in getChildren()) {
            desc.addChild(describeChild(item))
        }
        desc
    }

    override fun getChildren(): MutableList<SpekJUnitOnRunner<T>> = _children
    override fun getDescription(): Description? = _description

    protected override fun describeChild(child: SpekJUnitOnRunner<T>?): Description? {
        return child?.getDescription()
    }

    protected override fun runChild(child: SpekJUnitOnRunner<T>?, notifier: RunNotifier?) {
        testAction(describeChild(child)!!, notifier!!) {
            child!!.run(notifier!!)
        }
    }
}

public class SpekJUnitClassRunner<T>(val specificationClass: Class<T>) : ParentRunner<SpekJUnitGivenRunner<T>>(specificationClass) {
    private val suiteDescription = Description.createSuiteDescription(specificationClass)

    override fun getChildren(): MutableList<SpekJUnitGivenRunner<T>> = _children

    val _children by Delegates.lazy {
        if (javaClass<SpekImpl>().isAssignableFrom(specificationClass)) {
            val spek = specificationClass.newInstance() as SpekImpl
            val result = arrayListOf<SpekJUnitGivenRunner<T>>()
            spek.iterateGiven { result.add(SpekJUnitGivenRunner(specificationClass, it)) }
            result
        } else
         arrayListOf()
    }

    protected override fun describeChild(child: SpekJUnitGivenRunner<T>?): Description? {
        return child?.getDescription()
    }

    protected override fun runChild(child: SpekJUnitGivenRunner<T>?, notifier: RunNotifier?) {
        testAction(describeChild(child)!!, notifier!!) {
            child!!.run(notifier!!)
        }
    }
}
