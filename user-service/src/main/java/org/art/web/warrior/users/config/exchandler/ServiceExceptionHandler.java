package org.art.web.warrior.users.config.exchandler;

import org.art.web.warrior.commons.users.dto.UserDto;
import org.art.web.warrior.users.exception.RoleNotFoundException;
import org.art.web.warrior.users.exception.UserNotFoundException;
import org.art.web.warrior.users.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ServiceExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(er -> {
            String fieldName = er.getObjectName();
            String errorMessage = er.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class, RoleNotFoundException.class})
    public ResponseEntity<UserDto> handleNotFoundException(Exception e) {
        UserDto userDto = UserDto.builder()
                .firstName("Hello")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userDto);

    }
}
