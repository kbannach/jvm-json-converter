package ug.jvm.deserializer;

import static org.assertj.core.api.Assertions.assertThat;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import ug.jvm.mock.PlainPrimitives;
import util.ReflectionUtils;

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

   public void testGetItemsMethod() {
      try {
         // arrange
         Method method = ReflectionUtils.getPrivateMethod(JsonToStringMapConverter.class, "getItems", String.class);
         method.setAccessible(true);
         // act
         @SuppressWarnings("unchecked")
         List<String> list = (List<String>) method.invoke(null, this.jsonWithPrimitives_1);
         // assert
         assertThat(list.size()).isEqualTo(4);
         assertThat(list.get(0)).isEqualTo("number: 1");
         assertThat(list.get(1)).isEqualTo("longNumber: 1337");
         assertThat(list.get(2)).isEqualTo("active: true");
         assertThat(list.get(3)).isEqualTo("string: \"Moon Trance\"");
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
         assertTrue("Exception occured: " + e.getMessage(), false);
      }
   }

   public void testConvertingJsonToMap() {
      // act
      Map<String, String> map = JsonToStringMapConverter.convert(this.jsonWithPrimitives_1);
      // assert
      assertThat(map.size()).isEqualTo(4);
      assertThat(map.get("number")).isEqualTo("1");
      assertThat(map.get("longNumber")).isEqualTo("1337");
      assertThat(map.get("active")).isEqualTo("true");
      assertThat(map.get("string")).isEqualTo("\"Moon Trance\"");
   }
}