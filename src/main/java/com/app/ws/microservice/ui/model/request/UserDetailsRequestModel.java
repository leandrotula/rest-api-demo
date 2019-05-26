package com.app.ws.microservice.ui.model.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
public class UserDetailsRequestModel {

    @NotEmpty(message = "first name cannot be null")
	private String firstName;

    @NotEmpty(message = "last name cannot be null")
	private String lastName;
	private String email;
	private String password;
	private List<AddressRequestModel> addresses;

}
