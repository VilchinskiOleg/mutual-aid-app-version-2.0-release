package org.exception.handling.autoconfiguration.handler;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static org.exception.handling.autoconfiguration.utils.Constant.*;
import static org.springframework.util.CollectionUtils.isEmpty;

import lombok.extern.slf4j.Slf4j;
import org.exception.handling.autoconfiguration.model.BaseResponse;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.exception.handling.autoconfiguration.model.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    @Resource
    private ErrorMessagesManager errorMessagesManager;

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<BaseResponse> conflictExceptionHandler(HttpServletRequest request, ConflictException ex) {
        Error error= Error.builder()
                .code(ex.getMessage())
                .message(errorMessagesManager.getLocalizedErrorMessage(ex.getMessage(), extractLang(request)))
                .build();
        return ResponseEntity.status(ex.getCode())
                .body(new BaseResponse(error));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> validationExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException ex) {
        String lang = extractLang(request);
        List<ObjectError> validationErrorsData = ex.getAllErrors();
        List<Error> validationErrors = isEmpty(validationErrorsData) ?
                null : convertValidationErrorsDataToValidationErrors(validationErrorsData, lang);
        Error error= Error.builder()
                .code(VALIDATION_EXCEPTION_MESSAGE_CODE)
                .message(errorMessagesManager.getLocalizedErrorMessage(VALIDATION_EXCEPTION_MESSAGE_CODE, lang))
                .nestedErrors(validationErrors)
                .build();
        return ResponseEntity.status(VALIDATION_EXCEPTION_STATUS_CODE)
                .body(new BaseResponse(error));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<BaseResponse> commonExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("Unexpected error: cannot process this case.", ex);
        Error error= Error.builder()
                .code(COMMON_EXCEPTION_MESSAGE_CODE)
                .message(errorMessagesManager.getLocalizedErrorMessage(COMMON_EXCEPTION_MESSAGE_CODE, extractLang(request)))
                .build();
        return ResponseEntity.status(COMMON_EXCEPTION_STATUS_CODE)
                .body(new BaseResponse(error));
    }



    private List<Error> convertValidationErrorsDataToValidationErrors(List<ObjectError> validationErrorsData, String lang) {
        return validationErrorsData
                .stream()
                .map(errorData -> {
                    String errorMessageCode = errorData.getDefaultMessage();
                    String localizedErrorMessage = errorMessagesManager.getLocalizedErrorMessage(errorMessageCode, lang);
                    return Error
                            .builder()
                            .code(errorMessageCode)
                            .message(localizedErrorMessage)
                            .build();
                })
                .collect(toList());
    }

    private String extractLang(HttpServletRequest request) {
        Object lang = request.getHeader(LANG_HEADER);
        if (isNull(lang)) {
            return DEFAULT_LANG;
        }
        return lang.toString();
    }
}