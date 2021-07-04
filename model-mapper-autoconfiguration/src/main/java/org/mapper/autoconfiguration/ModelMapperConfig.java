package org.mapper.autoconfiguration;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.mapper.autoconfiguration.processor.AnnotationContextProcessor;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;
import java.util.List;

@Configuration
public class ModelMapperConfig {

    @Resource
    private List<BaseConverter<?, ?>> converters;

    @ConditionalOnMissingBean
    @Bean(name = "mapper")
    public Mapper configurerModelMapper() {
        Mapper mapper = new Mapper(new AnnotationContextProcessor());
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        converters.forEach(converter -> {
            converter.setMapper(mapper);
            mapper.addConverter(converter);
        });
        return mapper;
    }
}
