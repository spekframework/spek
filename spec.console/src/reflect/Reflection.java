package org.spek;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: hadihariri
 * Date: 12/16/12
 * Time: 8:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class Reflection {

    public String executeSpecification(Method method) throws InvocationTargetException, IllegalAccessException {

        if (method != null) {
            try {
                method.invoke(null, null);
                return null;
            } catch (InvocationTargetException e) {
                return e.getMessage();
            } catch (IllegalAccessException e) {
                return e.getMessage();
            }
        }
        return "No method found to run!";
    }

    public ArrayList<Method> getSpecifications(Iterable<Class> classes) throws IOException, ClassNotFoundException {


        ArrayList<Method> specifications = new ArrayList<Method>();

        for (Class c: classes) {
            for (Method m: c.getMethods()) {
                if (m.isAnnotationPresent(org.spek.spec.class)) {
                    specifications.add(m);
                }
            }
        }
        return specifications;

    }

}
