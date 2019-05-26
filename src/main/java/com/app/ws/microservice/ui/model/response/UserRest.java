package com.app.ws.microservice.ui.model.response;

import com.app.ws.microservice.shared.dto.AddressDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class UserRest {

	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private List<AddressRest> addresses;


}
