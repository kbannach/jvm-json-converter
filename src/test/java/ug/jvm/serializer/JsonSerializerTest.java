package ug.jvm.serializer;

import junit.framework.TestCase;
import ug.jvm.mock.CollectionPrimitives;
import ug.jvm.mock.PlainPrimitives;

import java.util.Arrays;
import java.util.List;

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
        PlainPrimitives object = mockPlainPrimitives(256, 1L, "Hey jude");

        // act
        String json = jsonSerializer.toJson(object);

        // assert
        assertThat(json).isEqualTo(
                "{ number: 256, longNumber: 1, string: \"Hey jude\", active: true }"
        );
    }

    public void testPrimitiveCollection() throws Exception {
        // arrange
        JsonSerializer jsonSerializer = new JsonSerializer();
        CollectionPrimitives collectionPrimitives = new CollectionPrimitives();
        collectionPrimitives.setList(Arrays.asList(1, 2, 3, 4, 1337));

        // act
        String json = jsonSerializer.toJson(collectionPrimitives);

        // assert
        assertThat(json).isEqualTo(
                "{ list: [1, 2, 3, 4, 1337] }"
        );
    }

    public void testCollectionOfPrimitives() throws Exception {
        // arrange
        JsonSerializer jsonSerializer = new JsonSerializer();
        List<Number> listOfNumbers = Arrays.asList(1, 2, 3, 4, 1337);
        List<String> listOfStrings = Arrays.asList("hey", "go", "sleep", "it's", "1am");

        // act
        String jsonArrayNumbers = jsonSerializer.toJson(listOfNumbers);
        String jsonArrayString = jsonSerializer.toJson(listOfStrings);

        // assert
        assertThat(jsonArrayNumbers).isEqualTo("[1, 2, 3, 4, 1337]");
        assertThat(jsonArrayString).isEqualTo("[\"hey\", \"go\", \"sleep\", \"it's\", \"1am\"]");
    }

    private PlainPrimitives mockPlainPrimitives(int number, long longNumber, String s) {
        PlainPrimitives object = new PlainPrimitives();
        object.setNumber(number);
        object.setLongNumber(longNumber);
        object.setString(s);
        object.setActive(true);

        return object;
    }
}