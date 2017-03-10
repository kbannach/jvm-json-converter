package ug.jvm.deserializer;

import static org.assertj.core.api.Assertions.assertThat;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import ug.jvm.mock.CollectionObjects;
import ug.jvm.mock.NestedObject;
import ug.jvm.mock.PlainPrimitives;
import ug.jvm.util.ReflectionUtils;

public class JsonDeserializerTest extends TestCase {

   private final String jsonWithPrimitives_1 = //
                                             "{ " + //
                                                   "number: 1, " + //
                                                   "longNumber: 1337, " + //
                                                   "active: true, " + //
                                                   "string: \"Moon Trance\"" + //
                                                   "}";
   private final String jsonWithPrimitives_2 = //
                                             "{ " + //
                                                   "number: 123123, " + //
                                                   "longNumber: 1337123321, " + //
                                                   "active: false, " + //
                                                   "string: null" + //
                                                   "}";

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

   public void testParsingJsonWithCorrectPrimitives_1() {
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

   public void testParsingJsonWithCorrectPrimitives_2() {
      // arrange
      JsonDeserializer jsonDeserializer = new JsonDeserializer();
      // act
      PlainPrimitives plainPrimitives = jsonDeserializer.fromJson(this.jsonWithPrimitives_2, PlainPrimitives.class);
      // assert
      assertThat(plainPrimitives).isNotNull().isInstanceOf(PlainPrimitives.class);
      assertThat(plainPrimitives.getNumber()).isEqualTo(123123);
      assertThat(plainPrimitives.getLongNumber()).isEqualTo(1337123321L);
      assertThat(plainPrimitives.getString()).isNull();
      assertThat(plainPrimitives.isActive()).isFalse();
   }

   public void testParsingJsonWithNestedObject_1() {
      // arrange
      String json = //
      "{ " + //
            "id: 111, " + //
            "plainPrimitives: " + jsonWithPrimitives_1 + //
            " }";
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

   public void testParsingJsonWithNestedObject_2() {
      // arrange
      String json = //
      "{ " + //
            "id: 111, " + //
            "plainPrimitives: " + jsonWithPrimitives_2 + //
            " }";
      JsonDeserializer jsonDeserializer = new JsonDeserializer();
      // act
      NestedObject nestedObject = jsonDeserializer.fromJson(json, NestedObject.class);
      // assert
      assertThat(nestedObject).isNotNull().isInstanceOf(NestedObject.class);
      assertThat(nestedObject.getId()).isEqualTo(111);
      assertThat(nestedObject.getPlainPrimitives()).isNotNull().isInstanceOf(PlainPrimitives.class);
      assertThat(nestedObject.getPlainPrimitives().getNumber()).isEqualTo(123123);
      assertThat(nestedObject.getPlainPrimitives().getLongNumber()).isEqualTo(1337123321L);
      assertThat(nestedObject.getPlainPrimitives().getString()).isNull();
      assertThat(nestedObject.getPlainPrimitives().isActive()).isFalse();
   }

   public void testParsingJsonWithCollectionObject() {
      // arrange
      String json = "{ list: [ " + jsonWithPrimitives_1 + ", " + jsonWithPrimitives_2 + " ] }";
      JsonDeserializer jsonDeserializer = new JsonDeserializer();
      // act
      CollectionObjects collectionObject = jsonDeserializer.fromJson(json, CollectionObjects.class);
      // assert
      assertThat(collectionObject).isNotNull().isInstanceOf(CollectionObjects.class);
      PlainPrimitives plainPrimitives_1 = collectionObject.getList().get(0);
      PlainPrimitives plainPrimitives_2 = collectionObject.getList().get(1);
      assertThat(plainPrimitives_1).isNotNull().isInstanceOf(PlainPrimitives.class);
      assertThat(plainPrimitives_1.getNumber()).isEqualTo(1);
      assertThat(plainPrimitives_1.getLongNumber()).isEqualTo(1337L);
      assertThat(plainPrimitives_1.getString()).isEqualTo("Moon Trance");
      assertThat(plainPrimitives_1.isActive()).isTrue();
      assertThat(plainPrimitives_2).isNotNull().isInstanceOf(PlainPrimitives.class);
      assertThat(plainPrimitives_2.getNumber()).isEqualTo(123123);
      assertThat(plainPrimitives_2.getLongNumber()).isEqualTo(1337123321L);
      assertThat(plainPrimitives_2.getString()).isNull();
      assertThat(plainPrimitives_2.isActive()).isFalse();
   }
}