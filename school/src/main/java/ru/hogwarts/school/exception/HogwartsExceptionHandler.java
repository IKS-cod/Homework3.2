package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HogwartsExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handlerNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(AvatarProcessingException.class)
    public ResponseEntity<String> handlerAvatarProcessingException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось прочитать аватарку из запроса или файла");
    }
}
