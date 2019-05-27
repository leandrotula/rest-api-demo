package com.app.ws.microservice.ui.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@ToString
@Setter
@Getter
public class AddressRest extends RepresentationModel {

    private String city;
    private String addressId;
    private String country;
    private String streetName;
}
