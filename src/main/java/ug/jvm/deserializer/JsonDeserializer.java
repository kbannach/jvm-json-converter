package ug.jvm.deserializer;

import static ug.jvm.json.JsonSyntax.ARRAY_END;
import static ug.jvm.json.JsonSyntax.ARRAY_START;
import static ug.jvm.json.JsonSyntax.OBJECT_END;
import static ug.jvm.json.JsonSyntax.OBJECT_START;
import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ug.jvm.json.JsonSyntax;
import ug.jvm.json.JsonSyntaxNotValidException;
import ug.jvm.reflection.BeanFieldUtils;

public class JsonDeserializer {

   private enum PRIMITIVE_TYPES_MAP {
         BOOLEAN("boolean", Boolean.class),
         BYTE("byte", Byte.class),
         CHAR("char", Character.class),
         SHORT("short", Short.class),
         INT("int", Integer.class),
         LONG("long", Long.class),
         FLOAT("float", Float.class),
         DOUBLE("double", Double.class);

      private String     name;
      private Class< ? > javaType;

      private PRIMITIVE_TYPES_MAP(String name, Class< ? > cls) {
         this.name = name;
         this.javaType = cls;
      }

      /**
       * @return a Class object representation of a primitive of given name
       */
      public static final Class< ? > get(String name) {
         for (PRIMITIVE_TYPES_MAP v : values()) {
            if (v.name.equals(name)) {
               return v.javaType;
            }
         }
         return null;
      }
   }

   public <T> T fromJson(String json, Class<T> type) {
      json = json.trim();
      if (wraps(json, ARRAY_START, ARRAY_END)) {
         // array
         // ignore
      } else if (wraps(json, OBJECT_START, OBJECT_END)) {
         // object
         Map<String, String> nameValueStringMap = fetchMapFromJson(json);
         return deserializeObject(type, nameValueStringMap);
      }
      throw new JsonSyntaxNotValidException("First character is not an object.\n Json: " + json);
   }

// TODO add comments inside this method
   @SuppressWarnings("unchecked")
   private <T> List<T> deserializeCollection(Class<T> genericType, List<String> arrayStrings) {
      List<T> collection = new ArrayList<>(arrayStrings.size());
      for (String item : arrayStrings) {
         if (BeanFieldUtils.isJsonPrimitive(field)) {
         }
         // TODO
//            collection.add(e);
      }
      // TODO
      return collection;
   }

   private <T> T deserializeObject(Class<T> type, Map<String, String> nameValueStringMap) {
      try {
         T instance = type.newInstance();
         for (Field f : type.getDeclaredFields()) {
            String newValueStr = nameValueStringMap.get(f.getName());
            if (newValueStr == null) {
               continue;
            } else if (newValueStr.startsWith("[")) {
               setCollectionValue(f, newValueStr, instance);
            } else if (newValueStr.startsWith("{")) {
               setObjectValue(f, newValueStr, instance);
            } else {
               setSimpleValue(f, newValueStr, instance);
            }
         }
         return instance;
      } catch (InstantiationException | IllegalAccessException | IntrospectionException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
         throw new RuntimeException("Error during deserialization.", e);
      }
   }

   /**
    * Builds {@code f} field setter and invokes it with an object parsed from
    * {@code newValueStr} json as a parameter.
    */
   private void setCollectionValue(Field f, String newValueStr, Object instance) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      Method setter = BeanFieldUtils.buildSetter(f);
      setter.invoke(instance, deserializeCollection(f.getGenericType().getClass(), fetchArrayFromJson(newValueStr)));
   }

   /**
    * Builds {@code f} field setter and invokes it with an object parsed from
    * {@code newValueStr} json as a parameter.
    */
   private void setObjectValue(Field f, String newValueStr, Object instance) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      Method setter = BeanFieldUtils.buildSetter(f);
      setter.invoke(instance, fromJson(newValueStr, f.getType()));
   }

   /**
    * Builds {@code f} field setter and invokes it with a {@code newValueStr} as
    * a parameter. If {@code f} is a primitive type field, then its value is set
    * by a boxed parameter.
    */
   private void setSimpleValue(Field f, String newValueStr, Object instance) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
         InstantiationException, NoSuchMethodException, SecurityException {
      Method setter = BeanFieldUtils.buildSetter(f);
      setter.invoke(instance, buildObjectValue(f, newValueStr));
   }

   /**
    * parses {@code newValueStr} using a field type's constructor
    */
   private Object buildObjectValue(Field f, String newValueStr) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
         SecurityException {
      Class< ? > fieldType = getObjectTypeFromPrimitive(f.getType());
      if (fieldType.equals(String.class)) {
         // remove quotes for strings
         newValueStr = ((String) newValueStr).substring(1, ((String) newValueStr).length() - 1);
      }
      return fieldType.getConstructor(String.class).newInstance(newValueStr);
   }

   private Class< ? > getObjectTypeFromPrimitive(Class< ? > fieldType) {
      if (fieldType.isPrimitive()) {
         fieldType = PRIMITIVE_TYPES_MAP.get(fieldType.getName());
      }
      return fieldType;
   }

   /**
    * @return a list representing json array
    */
   private List<String> fetchArrayFromJson(String json) {
      return JsonToStringMapConverter.convertArray(json);
   }

   /**
    * @return &lt;field name&gt; : &lt;string representing field value&gt; map
    */
   private Map<String, String> fetchMapFromJson(String json) {
      return JsonToStringMapConverter.convertMap(json);
   }

   private boolean wraps(String json, JsonSyntax startsWithElement, JsonSyntax endsWithElement) {
      return starts(json, startsWithElement) && ends(json, endsWithElement);
   }

   private boolean starts(String json, JsonSyntax syntaxElement) {
      return json.startsWith(syntaxElement.toString());
   }

   private boolean ends(String json, JsonSyntax syntaxElement) {
      return json.endsWith(syntaxElement.toString());
   }
}
