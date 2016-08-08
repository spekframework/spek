package org.jetbrains.spek.engine.support

import org.jetbrains.spek.engine.SpekTestEngine
import org.jetbrains.spek.engine.test.AbstractJUnitTestEngineTest

/**
 * @author Ranie Jade Ramiso
 */
abstract class AbstractSpekTestEngineTest: AbstractJUnitTestEngineTest<SpekTestEngine>(SpekTestEngine())
