package org.common.http.autoconfiguration.annotation;

import static org.common.http.autoconfiguration.utils.Constant.LANG_HEADER;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams(value = {
        @ApiImplicitParam(name = LANG_HEADER, value = "lang", example = "en", paramType = "header")
})
public @interface Api { }
