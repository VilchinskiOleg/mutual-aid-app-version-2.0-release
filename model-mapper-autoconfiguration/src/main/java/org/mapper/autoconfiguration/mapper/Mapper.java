package org.mapper.autoconfiguration.mapper;

import static java.lang.Enum.valueOf;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.math.NumberUtils.*;
import static org.mapper.autoconfiguration.utils.ReflectionUtils.retrieveGenericArgumentTypes;
import static org.springframework.util.CollectionUtils.isEmpty;

import org.mapper.autoconfiguration.exception.ModelMapperException;
import org.mapper.autoconfiguration.processor.ContextProcessor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import java.util.Collection;
import java.util.List;

public class Mapper extends ModelMapper implements MapperExtended {

    private final ContextProcessor contextProcessor;

    public Mapper(ContextProcessor contextProcessor) {
        super();
        this.contextProcessor = contextProcessor;
    }

    @Override
    public <E extends Collection<D>, D> E map(Collection<?> sources, E destinations, Class<D> destinationType) {
        if (!isEmpty(sources)) {
            sources.forEach(source -> destinations.add(super.map(source, destinationType)));
        }
        return destinations;
    }

    @Override
    public String map(Enum<?> source) {
        return source.toString();
    }

    @Override
    public <E extends Enum<E>> E map(String source, Class<E> type) {
        return valueOf(type, source);
    }

    @Override
    public <S, D> void addConverter(Converter<S, D> converter) {
        try {
            String context = contextProcessor.getContextIfExists(converter);
            if (isNotBlank(context)) {
                List<Class<?>> types = retrieveGenericArgumentTypes(converter);
                checkSourceDestinationTypes(types);
                super.createTypeMap(
                        (Class<S>) types.get(INTEGER_ZERO),
                        (Class<D>) types.get(INTEGER_ONE),
                        context).setConverter(converter);
            } else {
                super.addConverter(converter);
            }
        } catch (Exception ex) {
            throw new ModelMapperException(format("Unexpected error: Cannot registry converter: %s.", converter.getClass().getSimpleName()), ex);
        }
    }

    private void checkSourceDestinationTypes(List<Class<?>> types) {
        if (types.size() != INTEGER_TWO) {
            throw new ModelMapperException("Unexpected error: cannot process source and destination types.");
        }
    }
}
