package ug.jvm.serializer;

import junit.framework.TestCase;

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
}