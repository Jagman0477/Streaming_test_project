package com.stream.app.spring_stream_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {

    private String status;
    private String message;
    private Integer statusCode;
    private String statusName;
    private String timeStamp;

}
