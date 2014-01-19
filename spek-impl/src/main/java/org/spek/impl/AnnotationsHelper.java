package org.spek.impl;

import jet.runtime.typeinfo.KotlinSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Created by Eugene Petrenko (eugene.petrenko@gmail.com)
 * Date: 11.05.13 14:37
 */
public class AnnotationsHelper {
  @KotlinSignature("fun <T> getAnnotation(element: AnnotatedElement, clazz: Class<T>): T?")
  public static <T> T getAnnotation(AnnotatedElement element, Class<T> clazz) {
    //Dirty workaround for KT-3534
    if (!Annotation.class.isAssignableFrom(clazz)) throw new RuntimeException(clazz + " must be an annotation");
    Annotation annotation = element.getAnnotation((Class) clazz);
    if (annotation == null) return null;
    return clazz.cast(annotation);
  }

  @KotlinSignature("fun <T> toAnnotationClazz(clazz: Class<T>): Class<out Annotation>")
  public static <T> Class<? extends Annotation> toAnnotationClazz(Class<T> clazz) {
    //Dirty workaround for KT-3534
    if (!Annotation.class.isAssignableFrom(clazz)) throw new RuntimeException(clazz + " must be an annotation");
    //noinspection unchecked
    return (Class)clazz;
  }

  @KotlinSignature("fun <T> hasAnnotationClazz(element: AnnotatedElement?, clazz: Class<T>): Boolean")
  public static <T> boolean hasAnnotationClazz(AnnotatedElement element, Class<T> clazz) {
    //Dirty workaround for KT-3534
    if (!Annotation.class.isAssignableFrom(clazz)) throw new RuntimeException(clazz + " must be an annotation");
    //noinspection unchecked

    return element.isAnnotationPresent((Class)clazz);
  }
}
