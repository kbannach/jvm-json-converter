package ug.jvm.deserializer;

public class JsonNotCompatibleException extends RuntimeException {
    public JsonNotCompatibleException(String message) {
        super(message);
    }
}
