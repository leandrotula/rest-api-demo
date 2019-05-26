package com.app.ws.microservice.service;

import com.app.ws.microservice.shared.dto.AddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> findAddresses(final String userId);
}
