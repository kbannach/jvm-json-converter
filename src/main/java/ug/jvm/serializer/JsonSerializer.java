package ug.jvm.serializer;

public class JsonSerializer {

    public String toJson(Object src) {
        return JsonStringifyFactory.factory(src)
                .apply(src);
    }
}
