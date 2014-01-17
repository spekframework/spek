package org.spek.console.api

import org.spek.impl.*
import org.spek.reflect.DetectedSpek

public abstract class ConsoleSpek: SpekImpl(), DetectedSpek {
    override fun name(): String = javaClass.getName()
}
