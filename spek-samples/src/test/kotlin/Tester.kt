package org.spek.samples

import org.spek.Spek


class TimingSpecification: Spek() {{

    given("something") {
        beforeOn({println("beforeOn")})
        on("do something") {
            println("on")
            it("it", {
                    println("it 1")

                }
            )
            it("it", {
                println("it 2")

            })
        }

        afterOn({
            println("afterOn")
        })
    }


}}