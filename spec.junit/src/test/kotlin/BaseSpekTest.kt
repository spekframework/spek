package org.spek.junit.impl

import org.spek.junit.api.AbstractSpek

class DefaultSpek: AbstractSpek() {{
    given("a situation") {

        on("an event") {
            it("should A") { }
        }

        on("another event") {
            it("should B") {}
            it("should C") {}
            it("should D") {}
        }
    }
}}

class DefaultPendingItSpek: AbstractSpek() {{
    given("a situation") {
        on("an event") {
            it("should A") {
                pending("not implemented yet")
            }

            //default pending.
            it("should B")
        }
    }
}}

