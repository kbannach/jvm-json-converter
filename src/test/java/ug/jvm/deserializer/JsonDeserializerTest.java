package ug.jvm.deserializer;

import junit.framework.TestCase;
import ug.jvm.mock.PlainPrimitives;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonDeserializerTest extends TestCase {

    public void testParsingJsonWithCorrectPrimitives() {
        // arrange
        JsonDeserializer jsonDeserializer = new JsonDeserializer();
        String jsonWithPrimitives = "{ " +
                "number: 1, " +
                "longNumber: 1337, " +
                "active: true, " +
                "string: \"Moon Trance\"" +
                "}";
        // act
        PlainPrimitives plainPrimitives = jsonDeserializer.fromJson(jsonWithPrimitives, PlainPrimitives.class);

        // assert
        assertThat(plainPrimitives)
                .isNotNull()
                .isInstanceOf(PlainPrimitives.class);

        assertThat(plainPrimitives.getNumber()).isEqualTo(1);
        assertThat(plainPrimitives.getLongNumber()).isEqualTo(1337L);
        assertThat(plainPrimitives.getString()).isEqualTo("Moon Trance");
        assertThat(plainPrimitives.isActive()).isTrue();
    }

}