package com.stream.app.spring_stream_backend.exceptions;

import com.stream.app.spring_stream_backend.models.ErrorResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;
import java.util.UUID;

@ControllerAdvice
public class ExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponseModel> inputOutputException(IOException ioException) {
        String gUID = UUID.randomUUID().toString();
        LOGGER.debug(gUID);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseModel(gUID, ioException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), LocalDateTime.now().toString()));
    }

}
