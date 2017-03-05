package ug.jvm.util;

import java.lang.reflect.Method;

public enum ReflectionUtils {
   ;

   public static Method getPrivateMethod(Class< ? > clazz, String methodName, Class< ? >... params) {
      try {
         return clazz.getDeclaredMethod(methodName, params);
      } catch (NoSuchMethodException | SecurityException e) {
         throw new ReflectionException("Class " + clazz.getName() + ", method " + methodName, e);
      }
   }
}
