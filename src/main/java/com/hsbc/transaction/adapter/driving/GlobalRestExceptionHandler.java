package com.hsbc.transaction.adapter.driving;

import com.hsbc.transaction.common.exceptions.EntityNotExistException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ZhMM
 * @since 2025/3/10 23:28
 **/
@RestControllerAdvice
public class GlobalRestExceptionHandler {

    /**
     * 资源不存在异常
     *
     * @param e 异常
     * @return ResponseEntity<String>
     */
    @ExceptionHandler(EntityNotExistException.class)
    public ResponseEntity<String> handleEntityNotExistException(EntityNotExistException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * 请求参数验证异常
     *
     * @param e 异常
     * @return ResponseEntity<Map < String, String>>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * 其他服务内部异常
     *
     * @param e 异常
     * @return ResponseEntity<String>
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
