package org.mapper.autoconfiguration.converter;

import org.mapper.autoconfiguration.mapper.Mapper;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public abstract class BaseConverter<S, D> implements Converter<S, D> {

    @Setter
    protected Mapper mapper;

    @Override
    public D convert(MappingContext<S, D> mappingContext) {
        S source = mappingContext.getSource();
        D destination = getDestination();
        convert(source, destination);
        return destination;
    }

    protected abstract D getDestination();
    public abstract void convert(S source, D destination);
}
