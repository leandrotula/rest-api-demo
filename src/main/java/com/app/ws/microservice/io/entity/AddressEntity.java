package com.app.ws.microservice.io.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "addresses")
@Setter
@Getter
public class AddressEntity implements Serializable {

    @javax.persistence.Id
    @GeneratedValue
    private Long id;

    @Column(length=30, nullable=false)
    private String addressId;

    @Column(length=15, nullable=false)
    private String city;

    @Column(length=15, nullable=false)
    private String country;

    @Column(length=100, nullable=false)
    private String streetName;

    @Column(length=7, nullable=false)
    private String postalCode;

    @Column(length=10, nullable=false)
    private String type;

    @ManyToOne
    @JoinColumn(name="users_id")
    private UserEntity userDto;
}
