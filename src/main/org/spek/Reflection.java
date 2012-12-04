package org.spek;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


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

    public ArrayList<Method> getSpecifications(String packageName) throws IOException, ClassNotFoundException {


        ArrayList<Method> specifications = new ArrayList<Method>();

        if (!packageName.equals("")) {
            Iterable<Class> classes = getClasses(packageName);
            for (Class c: classes) {
                for (Method m: c.getMethods()) {
                    if (m.isAnnotationPresent(org.spek.spec.class)) {
                        specifications.add(m);
                    }
                }
            }
        }
        return specifications;

    }

    private Iterable<Class> getClasses(String packageName) throws ClassNotFoundException, IOException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements())
        {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        List<Class> classes = new ArrayList<Class>();
        for (File directory : dirs)
        {
            classes.addAll(findClasses(directory, packageName));
        }

        return classes;
    }

    private List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException
    {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists())
        {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files)
        {
            if (file.isDirectory())
            {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            }
            else if (file.getName().endsWith(".class"))
            {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

}
