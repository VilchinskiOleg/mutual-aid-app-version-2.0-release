package org.mapper.autoconfiguration.converter;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.mapper.autoconfiguration.utils.ReflectionUtils.retrieveGenericArgumentTypes;

import org.mapper.autoconfiguration.exception.ModelMapperException;
import org.mapper.autoconfiguration.mapper.Mapper;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public abstract class BaseConverter<S, D> implements Converter<S, D> {

    public static final String DEFAULT_CONSTRUCTOR_FAIL = "Unexpected error: cannot use default constructor.";

    @Setter
    protected Mapper mapper;

    @Override
    public D convert(MappingContext<S, D> mappingContext) {
        S source = mappingContext.getSource();
        D destination = mappingContext.getDestination();
        if (isNull(destination)) {
            destination = getDestination();
        }
        convert(source, destination);
        return destination;
    }

    public abstract void convert(S source, D destination);

    public Class<S> getSourceType() {
        List<Class<?>> types = retrieveGenericArgumentTypes(this);
        return (Class<S>) types.get(INTEGER_ZERO);
    }

    public Class<D> getDestinationType() {
        List<Class<?>> types = retrieveGenericArgumentTypes(this);
        return (Class<D>) types.get(INTEGER_ONE);
    }

    /**
     * must @return empty instance.
     */
    protected D getDestination() {
        try {
            return getDestinationType().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new ModelMapperException(DEFAULT_CONSTRUCTOR_FAIL);
        }
    }
}