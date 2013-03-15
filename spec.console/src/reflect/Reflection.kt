package org.spek.console.reflect;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hadihariri
 */
public class Reflection {
    public fun executeSpecification(method : Method) : String? {
        try {
            method.invoke(null, null);
            return null;
        } catch (InvocationTargetException e) {
            return e.getMessage();
        } catch (IllegalAccessException e) {
            return e.getMessage();
        }
    }

    public fun getSpecifications(classes : List<Class<*>>) : List<Method> {
        val specifications = arrayListOf<Method>();

        for (c in classes) {
            for (m in c.getMethods()) {
                //TODO: come up with annotation or whatever
                //if (m.isAnnotationPresent(org.spek.spec.class)) {
                    specifications.add(m);
                //}
            }
        }
        return specifications;
    }
}
