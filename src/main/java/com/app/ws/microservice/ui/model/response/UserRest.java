package com.app.ws.microservice.ui.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserRest {

	private String userId;
	private String firstName;
	private String lastName;
	private String email;

}
