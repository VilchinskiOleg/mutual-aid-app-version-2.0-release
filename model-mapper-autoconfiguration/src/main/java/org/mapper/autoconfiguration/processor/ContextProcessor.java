package org.mapper.autoconfiguration.processor;

import org.modelmapper.Converter;

public interface ContextProcessor {

    String getContextIfExists(Converter<?,?> converter);
}
