package com.app.ws.microservice.exceptions.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Field {

    private String field;
    private String message;
}
