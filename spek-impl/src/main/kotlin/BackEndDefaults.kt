package org.spek.impl

import org.spek.api.*

public trait SpekWithDefaults : Spek, SkipSupportWithDefaults {
    override fun given(description: String) {
        given(description) { pending() }
    }
}

public trait GivenWithDefaults : Given, SkipSupportWithDefaults {
    override fun on(description: String) {
        on(description) { pending() }
    }
}

public trait OnWithDefaults : On, SkipSupportWithDefaults {
    override fun it(description: String) {
        it(description) { pending() }
    }
}

public trait SkipSupportWithDefaults : SkipSupport {
    override fun skip() {
        skip("not given")
    }

    override fun pending() {
        pending("not given")
    }
}
