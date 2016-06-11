package org.jetbrains.spek.engine.scope

import java.util.*

/**
 * @author Ranie Jade Ramiso
 */
class Fixtures(var beforeEach: LinkedList<() -> Unit> = LinkedList(),
               var afterEach: LinkedList<() -> Unit> = LinkedList())
