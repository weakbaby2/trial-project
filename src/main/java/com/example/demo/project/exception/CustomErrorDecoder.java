package com.example.demo.project.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 500 && response.status() <= 599) {
            return new ResourceNotFoundException("Server is unavailable");
        }
        return new ErrorDecoder.Default().decode(methodKey, response);
    }
}
