package com.stream.app.spring_stream_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseModel {

    private String guid;
    private String message;
    private Integer statusCode;
    private String statusName;
    private String timeStamp;

}
