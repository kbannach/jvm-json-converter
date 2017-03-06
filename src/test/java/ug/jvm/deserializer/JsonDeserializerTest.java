package ug.jvm.deserializer;

import static org.assertj.core.api.Assertions.assertThat;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import ug.jvm.mock.NestedObject;
import ug.jvm.mock.PlainPrimitives;
import ug.jvm.util.ReflectionUtils;

// TODO kbannach add some tests (primitives, nested objects, recursive objects)
public class JsonDeserializerTest extends TestCase {

   private final String jsonWithPrimitives_1 = //
                                             "{ " + //
                                                   "number: 1, " + //
                                                   "longNumber: 1337, " + //
                                                   "active: true, " + //
                                                   "string: \"Moon Trance\"" + //
                                                   "}";

   public void testParsingJsonWithCorrectPrimitives() {
      // arrange
      JsonDeserializer jsonDeserializer = new JsonDeserializer();
      // act
      PlainPrimitives plainPrimitives = jsonDeserializer.fromJson(this.jsonWithPrimitives_1, PlainPrimitives.class);
      // assert
      assertThat(plainPrimitives).isNotNull().isInstanceOf(PlainPrimitives.class);
      assertThat(plainPrimitives.getNumber()).isEqualTo(1);
      assertThat(plainPrimitives.getLongNumber()).isEqualTo(1337L);
      assertThat(plainPrimitives.getString()).isEqualTo("Moon Trance");
      assertThat(plainPrimitives.isActive()).isTrue();
   }

   public void testGetItemsMethodWithJsonObject() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      // arrange
      Class< ? > jsonTypeEnum = JsonToStringMapConverter.class.getDeclaredClasses()[0];
      Object objectEnumValue = jsonTypeEnum.getEnumConstants()[0];
      Method method = ReflectionUtils.getPrivateMethod(JsonToStringMapConverter.class, "getItems", String.class, jsonTypeEnum);
      method.setAccessible(true);
      // act
      @SuppressWarnings("unchecked")
      List<String> list = (List<String>) method.invoke(null, this.jsonWithPrimitives_1, objectEnumValue);
      // assert
      assertThat(list.size()).isEqualTo(4);
      assertThat(list.get(0)).isEqualTo("number: 1");
      assertThat(list.get(1)).isEqualTo("longNumber: 1337");
      assertThat(list.get(2)).isEqualTo("active: true");
      assertThat(list.get(3)).isEqualTo("string: \"Moon Trance\"");
   }

   public void testGetItemsMethodWithJsonArray() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      // arrange
      Class< ? > enumJSON_TYPE = JsonToStringMapConverter.class.getDeclaredClasses()[0];
      Object arrayEnumValue = enumJSON_TYPE.getEnumConstants()[1];
      Method method = ReflectionUtils.getPrivateMethod(JsonToStringMapConverter.class, "getItems", String.class, enumJSON_TYPE);
      method.setAccessible(true);
      String json = //
      "[ " + //
            "number: 1, " + //
            "longNumber: 1337, " + //
            "active: true, " + //
            "string: \"Moon Trance\", " + //
            "array: [1, 2, 3]" + //
            "]";
      // act
      @SuppressWarnings("unchecked")
      List<String> list = (List<String>) method.invoke(null, json, arrayEnumValue);
      // assert
      assertThat(list.size()).isEqualTo(5);
      assertThat(list.get(0)).isEqualTo("number: 1");
      assertThat(list.get(1)).isEqualTo("longNumber: 1337");
      assertThat(list.get(2)).isEqualTo("active: true");
      assertThat(list.get(3)).isEqualTo("string: \"Moon Trance\"");
      assertThat(list.get(4)).isEqualTo("array: [1, 2, 3]");
   }

   public void testConvertingJsonToMap() {
      // act
      Map<String, String> map = JsonToStringMapConverter.convertMap(this.jsonWithPrimitives_1);
      // assert
      assertThat(map.size()).isEqualTo(4);
      assertThat(map.get("number")).isEqualTo("1");
      assertThat(map.get("longNumber")).isEqualTo("1337");
      assertThat(map.get("active")).isEqualTo("true");
      assertThat(map.get("string")).isEqualTo("\"Moon Trance\"");
   }

   public void testConvertingJsonWithArrayToMap() {
      // arrange
      String json = //
      "{ " + //
            "number: 1, " + //
            "longNumber: 1337, " + //
            "active: true, " + //
            "string: \"Moon Trance\", " + //
            "array: [1, 2, 3]" + //
            "}";
      // act
      Map<String, String> map = JsonToStringMapConverter.convertMap(json);
      // assert
      assertThat(map.size()).isEqualTo(5);
      assertThat(map.get("number")).isEqualTo("1");
      assertThat(map.get("longNumber")).isEqualTo("1337");
      assertThat(map.get("active")).isEqualTo("true");
      assertThat(map.get("string")).isEqualTo("\"Moon Trance\"");
      assertThat(map.get("array")).isEqualTo("[1, 2, 3]");
   }

   public void testConvertingJsonWithObjectToMap() {
      // arrange
      String json = //
      "{ " + //
            "number: 1, " + //
            "object: { test: 123 }" + //
            "}";
      // act
      Map<String, String> map = JsonToStringMapConverter.convertMap(json);
      // assert
      assertThat(map.size()).isEqualTo(2);
      assertThat(map.get("number")).isEqualTo("1");
      assertThat(map.get("object")).isEqualTo("{ test: 123 }");
   }

   public void testParsingJsonWithNestedObject() {
      // arrange
      String json = //
      "{ " + //
            "id: 111, " + //
            "plainPrimitives: " + jsonWithPrimitives_1 + //
            "}";
      JsonDeserializer jsonDeserializer = new JsonDeserializer();
      // act
      NestedObject nestedObject = jsonDeserializer.fromJson(json, NestedObject.class);
      // assert
      assertThat(nestedObject).isNotNull().isInstanceOf(NestedObject.class);
      assertThat(nestedObject.getId()).isEqualTo(111);
      assertThat(nestedObject.getPlainPrimitives().getNumber()).isEqualTo(1);
      assertThat(nestedObject.getPlainPrimitives().getLongNumber()).isEqualTo(1337L);
      assertThat(nestedObject.getPlainPrimitives().getString()).isEqualTo("Moon Trance");
      assertThat(nestedObject.getPlainPrimitives().isActive()).isTrue();
   }
}