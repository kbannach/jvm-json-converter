package ug.jvm;


import ug.jvm.deserializer.JsonDeserializer;
import ug.jvm.serializer.JsonSerializer;

public class JsonConverter {

    public String toJson(Object src) {
        return new JsonSerializer().toJson(src);
    }

    public <T> T fromJson(String json, Class<T> type) {
        return new JsonDeserializer().fromJson(json, type);
    }
}
