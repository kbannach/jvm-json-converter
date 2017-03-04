package ug.jvm;

import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.*;

public class JsonConverterTest extends TestCase {

    public void testToJson() {
        // arrange
        JsonConverter jsonConverter = new JsonConverter();

        // act
        String json = jsonConverter.toJson(new Object());

        // assert
        assertThat(json).isNotNull();

    }

    public void testFromJson() {
        // arrange

        // act

        // assert
        assertThat(true).isTrue();
    }

}