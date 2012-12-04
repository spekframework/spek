package spek


import java.lang.reflect.Method
import java.util.ArrayList
import org.jetbrains.spek.Reflection

public class SpecificationFinder {


    val reflector: Reflection = Reflection()

    public fun getSpecifications(packageName: String): List<Specification> {


        val methods = reflector.getSpecifications(packageName)

        val specifications = ArrayList<Specification>()

        if (methods != null) {
            methods filter { it != null} forEach { specifications.add(Specification(it!!))}
        }
        return specifications
    }


}
