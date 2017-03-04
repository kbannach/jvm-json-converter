package util;

import java.lang.reflect.Method;

public abstract class ReflectionUtils {

   public static Method getPrivateMethod(Class< ? > clazz, String methodName, Class< ? >... params) {
      try {
         return clazz.getDeclaredMethod(methodName, params);
      } catch (NoSuchMethodException | SecurityException e) {
         throw new ReflectionException("Class " + clazz.getName() + ", method " + methodName, e);
      }
   }
}
