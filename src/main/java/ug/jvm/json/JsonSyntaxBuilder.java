package ug.jvm.json;

import static ug.jvm.json.JsonSyntax.*;

public enum JsonSyntaxBuilder {
    ;

    public static String jsonArrayValue(String collectionResult) {
        return "" + ARRAY_START + collectionResult + ARRAY_END;
    }

    public static String jsonNullValue() {
        return JsonSyntax.NULL.toString();
    }

    public static String jsonStringValue(Object result) {
        return "" + STRING + result + STRING;
    }

    public static String jsonObjectValue(Object result) {
        return "" + OBJECT_START + result + OBJECT_END;
    }
}
