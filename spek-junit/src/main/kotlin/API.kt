package org.spek.junit.api

import org.spek.impl.*
import org.spek.junit.impl.*
import org.junit.runner.RunWith
import org.junit.Test as test

RunWith(javaClass<SpekJUnitClassRunner<*>>())
public abstract class JUnitSpek: SpekImpl() {}
