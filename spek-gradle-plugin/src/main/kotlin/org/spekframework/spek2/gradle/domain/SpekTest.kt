package org.spekframework.spek2.gradle.domain

import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SpekTest(val name: String) {
    internal var configurer by Delegates.notNull<SpekTest.() -> Unit>()
    private val compilationDelegate = object: ReadWriteProperty<SpekTest, KotlinCompilation<*>> {
        lateinit var value: KotlinCompilation<*>
        override fun getValue(thisRef: SpekTest, property: KProperty<*>): KotlinCompilation<*> {
            return value
        }

        override fun setValue(thisRef: SpekTest, property: KProperty<*>, value: KotlinCompilation<*>) {
            this.value = value
            configurer.invoke(this@SpekTest)
        }
    }

    var compilation by compilationDelegate
}