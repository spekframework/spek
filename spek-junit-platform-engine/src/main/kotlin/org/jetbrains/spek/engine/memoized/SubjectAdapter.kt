package org.jetbrains.spek.engine.memoized

import org.jetbrains.spek.api.memoized.CachingMode
import org.jetbrains.spek.extension.GroupExtensionContext
import org.jetbrains.spek.extension.TestExtensionContext
import org.jetbrains.spek.extension.execution.AfterExecuteGroup
import org.jetbrains.spek.extension.execution.AfterExecuteTest
import java.util.*

/**
 * Adapter for [subjects][Subject] as a pseudo-extension.
 *
 * @author Ranie Jade Ramiso
 */
class SubjectAdapter: AfterExecuteGroup, AfterExecuteTest {
    private val subjectMap: MutableMap<GroupExtensionContext, SubjectImpl<*>> = WeakHashMap()

    override fun afterExecuteGroup(group: GroupExtensionContext) {
        val subject = subjectMap[group]

        if (subject != null && subject.mode == CachingMode.GROUP) {
            subject.reset()
        }
    }

    override fun afterExecuteTest(test: TestExtensionContext) {
        if (!test.parent.lazy) {
            resetSubjects(test.parent)
        }
    }

    fun <T> registerSubject(mode: CachingMode, group: GroupExtensionContext, factory: () -> T): SubjectImpl<T> {
        val subject = SubjectImpl(mode, factory)
        subjectMap.put(group, subject)
        return subject
    }

    fun resetSubjects(group: GroupExtensionContext) {
        val subject = subjectMap[group]

        if (subject != null && subject.mode == CachingMode.TEST) {
            subject.reset()
        }

        if (group.parent != null) {
            resetSubjects(group.parent!!)
        }
    }
}
