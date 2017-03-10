package ug.jvm.serializer;

import java.util.Collection;
import java.util.stream.Collectors;

import static ug.jvm.json.JsonSyntaxBuilder.jsonArrayValue;

public class JsonArraySerializer {
    public String serialize(Collection<?> collection) {
        String collect = collection.stream()
                .map(this::parseField)
                .collect(Collectors.joining(", "));
        return jsonArrayValue(collect);
    }

    private String parseField(Object object) {
        return JsonStringifyFactory.factory(object)
                .apply(object);
    }
}
