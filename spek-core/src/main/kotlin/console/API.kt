package org.spek.console.api

import org.spek.impl.*
import java.util.HashMap

public abstract class ConsoleSpek: SpekImpl(), TestSpekAction {
    override fun description(): String = javaClass.getName()
}
