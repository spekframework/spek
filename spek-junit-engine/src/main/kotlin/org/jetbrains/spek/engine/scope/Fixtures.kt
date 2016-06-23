package org.jetbrains.spek.engine.scope

import org.jetbrains.spek.api.dsl.TestBody
import java.util.*

/**
 * @author Ranie Jade Ramiso
 */
class Fixtures(var beforeEach: LinkedList<TestBody.() -> Unit> = LinkedList(),
               var afterEach: LinkedList<() -> Unit> = LinkedList())
