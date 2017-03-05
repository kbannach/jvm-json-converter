package ug.jvm.reflection;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

public enum BeanFieldUtils {
   ;

   public static Object getValue(Field field, Object instance) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
      Method method = buildGetter(field);
      if (method == null) {
         throw new RuntimeException("Getter not found | field: " + field.getName());
      }
      return method.invoke(instance);
   }

   // Source: http://stackoverflow.com/a/2638662/2757140
   public static Method buildGetter(Field field) throws IntrospectionException {
      Class< ? > type = field.getDeclaringClass();
      return new PropertyDescriptor(field.getName(), type).getReadMethod();
   }

   /**
    * @return setter analogical to a method {@code buildGetter}
    */
   public static Method buildSetter(Field field) throws IntrospectionException {
      Class< ? > type = field.getDeclaringClass();
      return new PropertyDescriptor(field.getName(), type).getWriteMethod();
   }

   public static boolean isJsonPrimitive(Field field) {
      Class< ? > type = field.getType();
      // TODO: Check if Long.class, int, long, Integer.class, boolean works here
      return type.isAssignableFrom(Number.class) || type.isAssignableFrom(Boolean.class);
   }

   public static boolean isString(Field field) {
      Class< ? > type = field.getType();
      return type.isAssignableFrom(String.class);
   }

   public static boolean isCollection(Field field) {
      return Collection.class.isAssignableFrom(field.getType());
   }
}
