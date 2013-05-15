package org.spek.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Created by Eugene Petrenko (eugene.petrenko@gmail.com)
 * Date: 11.05.13 14:37
 */
public class AnnotationsHelper {
  public static <T> T getAnnotation(AnnotatedElement element, Class<T> clazz) {
    //Dirty workaround for KT-3534
    if (!Annotation.class.isAssignableFrom(clazz)) throw new RuntimeException(clazz + " must be an annotation");
    return clazz.cast(element.getAnnotation((Class)clazz));
  }

  public static <T> Class<? extends Annotation> toAnnotationClazz(Class<T> clazz) {
    //Dirty workaround for KT-3534
    if (!Annotation.class.isAssignableFrom(clazz)) throw new RuntimeException(clazz + " must be an annotation");
    //noinspection unchecked
    return (Class)clazz;
  }

  public static <T> boolean hasAnnotationClazz(AnnotatedElement element, Class<T> clazz) {
    //Dirty workaround for KT-3534
    if (!Annotation.class.isAssignableFrom(clazz)) throw new RuntimeException(clazz + " must be an annotation");
    //noinspection unchecked

    return element.isAnnotationPresent((Class)clazz);
  }
}
