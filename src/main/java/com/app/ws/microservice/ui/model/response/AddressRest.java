package com.app.ws.microservice.ui.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class AddressRest {

    private String city;
    private String country;
    private String streetName;
}
