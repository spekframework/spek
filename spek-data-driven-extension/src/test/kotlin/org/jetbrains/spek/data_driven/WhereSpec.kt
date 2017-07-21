package org.jetbrains.spek.data_driven

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

/**
 * @author Niels Falk
 */
object WhereSpec : Spek({
    describe("analyze names") {
        on("%s ", { name, expectedLen, expectPalindrome ->
            it("has a length $expectedLen") {
                assertEquals(expectedLen, name.length)
            }
            it("${expectPalindrome.isIsNot()} a palindrome") {
                assertEquals(expectPalindrome, name.isPalindrome())
            }
        }, where<String,Int,Boolean> {
            //@formatter:off
            //"name"  / "expectedLen" / "expectPalindrome"
            ! "Niels" / 5             / false
            ! "Max"   / 3             / false
            ! "Anna"  / 4             / true
            //@formatter:on
        })
    }
})

private fun Boolean.isIsNot() = (if (this) "is" else "is not")
private fun String.isPalindrome(): Boolean = toLowerCase().equals(toLowerCase().reversed())
