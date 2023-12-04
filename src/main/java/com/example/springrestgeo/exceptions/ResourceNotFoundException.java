package com.example.springrestgeo.exceptions;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {

    @Serial
    private static final Integer serialVersionUID =1;


    public ResourceNotFoundException(String message) {
        super(message);

    }

}