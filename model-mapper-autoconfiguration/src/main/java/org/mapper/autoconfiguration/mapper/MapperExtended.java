package org.mapper.autoconfiguration.mapper;

import java.util.Collection;

public interface MapperExtended {

    <E extends Collection<D>, D> E map(Collection<?> sources, E destinations, Class<D> destinationType);

    String map(Enum<?> source);

    <E extends Enum<E>> E map(String source, Class<E> type);
}
