package org.spek.junit.api

import org.spek.impl.*
import org.spek.junit.impl.*
import org.junit.runner.RunWith
import org.junit.Test as test
import org.spek.console.api.ConsoleSpek

RunWith(javaClass<JSpec<*>>())
public abstract class JUnitSpek: ConsoleSpek() {

    //possible workaround to cheat JUnit integration
    test public fun mockTest() {
    }
}

public abstract class AbstractSpek: JUnitSpek()


