package org.spek

import java.lang.reflect.Method
import org.spek.Reflection

public class Specification(val method: Method) {

    public fun run(output: Listener) {

        val reflection = Reflection()
        val methodName = method.getName()
        if (methodName != null) {
            val error = reflection.executeSpecification(method)
            if (error != null) {
                output.notify(SpecificationErrorOccurred(error))
            } else {
                output.notify(SpecificationExecuted())
            }
        }

    }
}

