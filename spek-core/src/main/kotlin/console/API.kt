package org.spek.console

import org.spek.impl.*
import org.spek.*

public abstract class ConsoleSpek : Spek(), TestSpekAction {
    override fun description(): String = javaClass.getName()
}
