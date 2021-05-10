package org.mapper.autoconfiguration.mapper;

import static java.lang.Enum.valueOf;

import org.modelmapper.ModelMapper;
import java.util.Collection;

public class Mapper extends ModelMapper implements MapperExtended {

    public Mapper() {
        super();
    }

    @Override
    public <E extends Collection<D>, D> E map(Collection<?> sources, E destinations, Class<D> destinationType) {
        sources.forEach(source -> destinations.add(super.map(source, destinationType)));
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
}
