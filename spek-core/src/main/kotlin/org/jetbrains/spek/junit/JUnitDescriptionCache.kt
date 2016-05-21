package org.jetbrains.spek.junit

import org.jetbrains.spek.api.ActionType
import org.jetbrains.spek.api.SpekTree
import org.junit.runner.Description
import java.io.Serializable
import java.util.*

data class JUnitUniqueId(val id: Int) : Serializable {
    companion object {
        var id = 0
        fun next() = JUnitUniqueId(id++)
    }
}

class JUnitDescriptionCache() {
    val cache: IdentityHashMap<SpekTree, Description> = IdentityHashMap()

    fun get(key: SpekTree): Description {
        return cache.getOrPut(key, { create(key) })
    }

    private fun create(key: SpekTree): Description {
        when (key.type) {
            ActionType.IT ->
                return Description.createSuiteDescription(key.description, JUnitUniqueId.next())
            ActionType.DESCRIBE -> {
                val description = Description.createSuiteDescription(key.description, JUnitUniqueId.next())
                key.children.forEach {
                    description.addChild(this.get(it))
                }
                return description
            }
        }
    }

}