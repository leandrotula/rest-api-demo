package com.app.ws.microservice.exceptions.dto;

import lombok.*;
import org.springframework.validation.FieldError;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class ErrorMessages {

    private Date timestamp;
    private String message;
    private List<Field> errors = new LinkedList<>();

}
