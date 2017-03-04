package ug.jvm.deserializer;

import ug.jvm.json.JsonSyntax;
import ug.jvm.json.JsonSyntaxNotValidException;

import static ug.jvm.json.JsonSyntax.*;

public class JsonDeserializer {

    public <T> T fromJson(String json, Class<T> type) {
        json = json.trim();
        if (wraps(json, ARRAY_START, ARRAY_END)) {
            // array
        } else if (wraps(json, OBJECT_START, OBJECT_END)) {
            // object
        }

        throw new JsonSyntaxNotValidException("First character is not array nor object.\n Json: " + json);
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
