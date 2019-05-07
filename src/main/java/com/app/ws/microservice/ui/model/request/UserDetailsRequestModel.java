package com.app.ws.microservice.ui.model.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@EqualsAndHashCode
public class UserDetailsRequestModel {
	
	private String firstName;
	private String lastName;
	private String email;
	private char[] password;

}
