package spek


import java.lang.reflect.Method
import java.util.ArrayList

public class SpecificationFinder {


    val reflector: Reflection = Reflection()

    public fun getSpecifications(packageName: String): List<Specification> {


        val methods = reflector.getSpecifications(packageName)

        val specifications = ArrayList<Specification>()

        if (methods != null) {
            val methodList = methods.iterator()
            for (m in methodList) {
                if (m != null) {
                    specifications.add(Specification(m))
                }
            }
        }
        return specifications
    }


}
