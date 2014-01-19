package org.spek.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Eugene Petrenko (eugene.petrenko@gmail.com)
 * Date: 11.05.13 14:44
 * TODO: the parameter (why) could be optional but due to this bug (#KT-3197),
 * TODO: I had no choice but to make it mandatory for now.
 * TODO: need to be refactored into Kotlin when #KT-3197 got fixed.
 * TODO: move to annotations package
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface skip {
  public String value();
}
