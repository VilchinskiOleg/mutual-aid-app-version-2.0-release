package core;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.implementation.FieldAccessor;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.tms.profile_service_core.domain.model.Name;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenerateProxyUsingByteBuddyTest {

    @Test
    void main()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException
    {

        String additionalFieldName = "additionalField";

        // Define the annotation :
        AnnotationDescription randomAnnotation = AnnotationDescription.Builder
                .ofType(Deprecated.class)
                .build();

        Class<?> dynamicProxyType = new ByteBuddy()
                .subclass(Name.class)
                .name("org.tms.profile_service_core.domain.model.ProxyName")

                // Add a field :
                .defineField(additionalFieldName, String.class, Modifier.PRIVATE)
                // Add a getter method for the field :
                .defineMethod("get" + StringUtils.capitalize(additionalFieldName), String.class, Modifier.PUBLIC)
                .intercept(FieldAccessor.ofField(additionalFieldName))
                .annotateMethod(randomAnnotation)
                // Add a setter method for the field
                .defineMethod("set" + StringUtils.capitalize(additionalFieldName), void.class, Modifier.PUBLIC)
                .withParameter(String.class)
                .intercept(FieldAccessor.ofField(additionalFieldName))

                .make()
                .load(Name.class.getClassLoader())
                .getLoaded();

        // Validate :
        // Create an instance of the dynamic class
        Name dynamicProxyObject = (Name) dynamicProxyType.getDeclaredConstructor().newInstance();

        // Set the field value
        Method setFieldMethod = dynamicProxyType.getMethod("set" + StringUtils.capitalize(additionalFieldName), String.class);
        setFieldMethod.invoke(dynamicProxyObject, "Field value was set by ByteBuddy");

        // Get the field value
        Method getFieldMethod = dynamicProxyType.getMethod("get" + StringUtils.capitalize(additionalFieldName));
        String fieldValue = (String) getFieldMethod.invoke(dynamicProxyObject);

        assertEquals("Field value was set by ByteBuddy", fieldValue);
        assertTrue(getFieldMethod.isAnnotationPresent(Deprecated.class));
        System.out.println("Field Value: " + fieldValue);
        System.out.println("Field Annotation: " + getFieldMethod.getAnnotation(Deprecated.class));
    }
}
