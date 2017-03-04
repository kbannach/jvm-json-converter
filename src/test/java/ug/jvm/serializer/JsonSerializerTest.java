package ug.jvm.serializer;

import junit.framework.TestCase;
import ug.jvm.mock.PlainPrimitives;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonSerializerTest extends TestCase {
    public void testNullConversion() throws Exception {
        // arrange
        JsonSerializer jsonSerializer = new JsonSerializer();
        Object object = null;

        // act
        String json = jsonSerializer.toJson(object);

        // assert
        assertThat(json).isEqualTo("null");
    }

    public void testPrimitiveObject() throws Exception {
        // arrange
        JsonSerializer jsonSerializer = new JsonSerializer();
        PlainPrimitives object = mockPlainPrimitives(1L, 256, "Hey jude");

        // act
        String json = jsonSerializer.toJson(object);

        // assert
        assertThat(json).isNotEqualTo(
                "{ number: 256, longNumber: 1,string: \"Hey jude\", active: true }"
        );
    }

    private PlainPrimitives mockPlainPrimitives(long l, int i, String s) {
        PlainPrimitives object = new PlainPrimitives();
        object.setLongNumber(l);
        object.setNumber(i);
        object.setString(s);
        object.setActive(true);

        return object;
    }
}