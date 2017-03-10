package ug.jvm;

import com.google.gson.Gson;
import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonConverterTest extends TestCase {

    public void testToJson() {
        // arrange
        JsonConverter jsonConverter = new JsonConverter();
        Gson gson = new Gson();
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