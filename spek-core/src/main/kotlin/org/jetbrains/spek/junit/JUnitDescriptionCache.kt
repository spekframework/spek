package org.jetbrains.spek.junit

import org.jetbrains.spek.api.ActionType
import org.jetbrains.spek.api.TestAction
import org.junit.runner.Description
import java.util.*


//data class JUnitUniqueId(val id: Int) : Serializable {
//    companion object {
//        var id = 0
//        fun next() = JUnitUniqueId(id++)
//    }
//}

class JUnitDescriptionCache() {
    val cache: IdentityHashMap<TestAction, Description> = IdentityHashMap()

    fun get(key: TestAction): Description {
        return cache.getOrPut(key, { create(key) })
    }

    private fun create(key: TestAction): Description {
        when (key.type()) {
            ActionType.IT ->
                return Description.createSuiteDescription(key.description())
            ActionType.DESCRIBE -> {
                val description = Description.createSuiteDescription(key.description())  // TODO: use a unique ID to prevent things going bad if two tests have the same name -- See JUnitUniqueId at the top of this file
                key.children().forEach {
                    description.addChild(this.get(it))
                }
                return description
            }
        }
    }

}