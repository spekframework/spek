package org.spek


import java.lang.reflect.Method
import java.util.ArrayList
import org.spek.Reflection

public class SpecificationFinder {

    public fun getSpecifications(folder: String, packageName: String): List<Specification> {

        val reflector: Reflection = Reflection()
        val classLoader: FileClassLoader = FileClassLoader()

        val classes = classLoader.getClasses(folder, packageName)
        val methods = reflector.getSpecifications(classes)

        val specifications = ArrayList<Specification>()

        if (methods != null) {
            methods filter { it != null} forEach { specifications.add(Specification(it!!))}
        }
        return specifications
    }


}
