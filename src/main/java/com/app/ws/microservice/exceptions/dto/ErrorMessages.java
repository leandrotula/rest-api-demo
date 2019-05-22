package com.app.ws.microservice.exceptions.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Field> errors = new LinkedList<>();

}
