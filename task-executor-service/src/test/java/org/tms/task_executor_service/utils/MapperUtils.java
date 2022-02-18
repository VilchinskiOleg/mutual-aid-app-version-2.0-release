package org.tms.task_executor_service.utils;

import java.util.List;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.mapper.autoconfiguration.processor.AnnotationContextProcessor;
import org.modelmapper.convention.MatchingStrategies;

public class MapperUtils {

    public static Mapper getMapper(List<BaseConverter> converters) {
        Mapper mapper = new Mapper(new AnnotationContextProcessor());
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        registerConverter(mapper, converters);
        return mapper;
    }

    private static void registerConverter(Mapper mapper, List<BaseConverter> converters) {
        converters.forEach(converter -> {
            converter.setMapper(mapper);
            mapper.addConverter(converter);
        });
    }
}