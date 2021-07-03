package org.mapper.autoconfiguration.processor;

import static java.util.Objects.nonNull;

import org.mapper.autoconfiguration.annotation.ModelMapperContext;
import org.modelmapper.Converter;
import org.springframework.lang.Nullable;

public class AnnotationContextProcessor implements ContextProcessor {

    @Nullable
    @Override
    public String getContextIfExists(Converter<?, ?> converter) {
        var context = converter.getClass().getAnnotation(ModelMapperContext.class);
        if (nonNull(context)) {
            return context.value();
        }
        return null;
    }
}
