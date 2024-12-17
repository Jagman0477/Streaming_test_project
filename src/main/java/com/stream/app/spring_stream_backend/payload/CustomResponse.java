package com.stream.app.spring_stream_backend.payload;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CustomResponse {

    private String message;
    private Boolean success = false;

}
