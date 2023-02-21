package org.tms.task_executor_service.utils;

import static java.util.List.of;

import java.util.Arrays;
import java.util.List;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.mapper.autoconfiguration.processor.AnnotationContextProcessor;
import org.modelmapper.convention.MatchingStrategies;
import org.tms.task_executor_service.mapper.DataMetaToMetaConverter;
import org.tms.task_executor_service.mapper.MetaToDataMetaConverter;
import org.tms.task_executor_service.mapper.payload.CreateProfilePayloadToDataCreateProfilePayloadConverter;
import org.tms.task_executor_service.mapper.payload.DataCreateProfilePayloadToCreateProfilePayloadConverterVisitor;
import org.tms.task_executor_service.mapper.service.PayloadMappingManager;

/**
 * Util class for interacting with @mapper and @converters beyond Spring.
 */
public class MapperUtils {

    public static Mapper getMapper() {
        Mapper mapper = new Mapper(new AnnotationContextProcessor());
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

    public static Mapper getMapper(List<BaseConverter> converters) {
        Mapper mapper = getMapper();
        registerConverter(mapper, converters);
        return mapper;
    }

    public static Mapper initDefaultTaskMapper(Mapper mapper, PayloadMappingManager payloadMappingManager, BaseConverter  ... taskConverters) {
        List<BaseConverter> payloadConverters = of( new CreateProfilePayloadToDataCreateProfilePayloadConverter(),
                                                    new DataCreateProfilePayloadToCreateProfilePayloadConverterVisitor());
        /**
         * Register @payloadConverters to @payloadMappingManager manually:
         */
        payloadConverters.forEach(payloadConverter -> payloadMappingManager.registerDestinationType(payloadConverter.getSourceType(),
                                                                                                    payloadConverter.getDestinationType()));

        List<BaseConverter> metaConverters = of(new DataMetaToMetaConverter(), new MetaToDataMetaConverter());

        registerConverter(mapper, Arrays.asList(taskConverters));
        registerConverter(mapper, payloadConverters);
        registerConverter(mapper, metaConverters);
        return mapper;
    }

    private static void registerConverter(Mapper mapper, List<BaseConverter> converters) {
        converters.forEach(converter -> {
            converter.setMapper(mapper);
            mapper.addConverter(converter);
        });
    }
}