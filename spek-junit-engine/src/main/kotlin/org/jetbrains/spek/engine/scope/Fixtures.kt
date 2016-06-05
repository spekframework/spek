package org.jetbrains.spek.engine.scope

/**
 * @author Ranie Jade Ramiso
 */
class Fixtures(var beforeEach: (() -> Unit)? = null, var afterEach: (() -> Unit)? = null)
