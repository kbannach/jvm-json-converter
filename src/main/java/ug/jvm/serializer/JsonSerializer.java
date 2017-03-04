package ug.jvm.serializer;

import ug.jvm.reflection.BeanFieldUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JsonSerializer {

    public String toJson(Object src) {
        if (src == null) {
            return "null";
        }

        return "{ " + fillFields(src) + " }";
    }

    private String fillFields(Object src) {
        Class<?> aClass = src.getClass();
        List<Field> fields = Arrays.asList(aClass.getDeclaredFields());
        return fields.stream()
                .map(field -> field.getName() + ": " + stringFieldValue(field, src))
                .collect(Collectors.joining(", "));
    }

    private String stringFieldValue(Field field, Object src) {
        Object result = getFieldValue(field, src);
        return buildFieldValue(field, result);
    }

    private Object getFieldValue(Field field, Object src) {
        try {
            return BeanFieldUtils.getValue(field, src);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal access | field: " + field.getName(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Can't get result of method | field: " + field.getName(), e);
        } catch (IntrospectionException e) {
            throw new RuntimeException("Can't build getter | field: " + field.getName(), e);
        }
    }

    private String buildFieldValue(Field field, Object result) {
        if (result == null) {
            return "null";
        }
        if (BeanFieldUtils.isString(field)) {
            return "\"" + result + "\"";
        }

        return result.toString();
    }


}
