package org.exception.handling.autoconfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.exception.handling.autoconfiguration")
public class ExceptionHandlingConfig {
}
