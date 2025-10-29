package de.szut.lf8_starter.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class QualificationNotMatchException extends RuntimeException {
    public QualificationNotMatchException(String message) {
        super(message);
    }
}
