package org.mapper.autoconfiguration.utils;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.getField;

import org.springframework.lang.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class ReflectionUtils {

    @Nullable
    public static Object retrieveFieldValueForClass(Object target, String fieldName) {
        return retrieveFieldValueForClass(target, target.getClass(), fieldName);
    }

    @Nullable
    public static Object retrieveFieldValueForClass(Object target, Class<?> targetType, String fieldName) {
        Field field = findField(targetType, fieldName);
        if (isNull(field)) {
            return null;
        }
        return retrieveValueForField(field, target);
    }

    @Nullable
    public static Object retrieveValueForField(Field field, Object instance) {
        field.setAccessible(true);
        return getField(field, instance);
    }

    public static List<Class<?>> retrieveGenericArgumentTypes(Object target) {
        ParameterizedType genericWrapper = (ParameterizedType) target.getClass().getGenericSuperclass();
        if (isNull(genericWrapper)){
            return emptyList();
        }
        return stream(genericWrapper.getActualTypeArguments())
                .map(type -> (Class<?>) type)
                .collect(toList());
    }
 }
