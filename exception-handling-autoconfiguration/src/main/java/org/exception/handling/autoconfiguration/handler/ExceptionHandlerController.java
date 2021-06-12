package org.exception.handling.autoconfiguration.handler;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.exception.handling.autoconfiguration.utils.Constant.*;
import static org.springframework.util.CollectionUtils.isEmpty;

import lombok.extern.slf4j.Slf4j;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.exception.handling.autoconfiguration.model.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    private final Map<String, String> localisationErrorMessagesConfig = new HashMap<>();

    @PostConstruct
    private void initLocalisationErrorsMessagesConfig() {
//        URL url = ClassLoader.getPlatformClassLoader()
//                             .getResource("errors.properties");
//        if (isNull(url)) {
//            throw new RuntimeException("Error on starting app: cannot build URL for resource 'errors.properties'");
//        }
//        String path = url.getPath();
//        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
//            bufferedReader.lines()
//                          .forEach(this::processErrorMessagesPropertiesLine);
//        } catch (IOException ex) {
//            throw new RuntimeException("Error on staring app: cannot read 'errors.properties' file", ex);
//        }
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<Error> ConflictExceptionHandler(ServletRequest request, ConflictException ex) {
        Error error= Error.builder()
                .code(ex.getMessage())
                .message(localisationErrorMessagesConfig.get(ex.getMessage()))
                .build();
        return ResponseEntity.status(ex.getCode())
                .body(error);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Error> ValidationExceptionHandler(ServletRequest request, MethodArgumentNotValidException ex) {
        List<ObjectError> validationErrorsData = ex.getAllErrors();
        List<Error> validationErrors = isEmpty(validationErrorsData) ?
                null : convertValidationErrorsDataToValidationErrors(validationErrorsData);
        Error error= Error.builder()
                .code(VALIDATION_EXCEPTION_MESSAGE_CODE)
                .message(localisationErrorMessagesConfig.get(VALIDATION_EXCEPTION_MESSAGE_CODE))
                .nestedErrors(validationErrors)
                .build();
        return ResponseEntity.status(VALIDATION_EXCEPTION_STATUS_CODE)
                .body(error);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Error> CommonExceptionHandler(ServletRequest request, Exception ex) {
        log.error("Unexpected error: cannot process this case.", ex);
        Error error= Error.builder()
                .code(COMMON_EXCEPTION_MESSAGE_CODE)
                .message(localisationErrorMessagesConfig.get(COMMON_EXCEPTION_MESSAGE_CODE))
                .build();
        return ResponseEntity.status(COMMON_EXCEPTION_STATUS_CODE)
                .body(error);
    }



    private void processErrorMessagesPropertiesLine(String line) {
        String[] result = line.split(EQUAL);
        if (result.length != 2) {
            log.error("Unexpected error: cannot process property: {}", line);
            return;
        }
        localisationErrorMessagesConfig.put(result[0].strip(), result[1].strip());
    }

    private List<Error> convertValidationErrorsDataToValidationErrors(List<ObjectError> validationErrorsData) {
        return validationErrorsData
                .stream()
                .map(errorData -> {
                    String errorMessageCode = errorData.getDefaultMessage();
                    String localizedErrorMessage = localisationErrorMessagesConfig.get(errorMessageCode);
                    return Error
                            .builder()
                            .code(errorMessageCode)
                            .message(localizedErrorMessage)
                            .build();
                })
                .collect(toList());
    }
}
