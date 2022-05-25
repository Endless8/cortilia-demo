package com.example.cortiliademo.v2.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {

    private String street;
    private String number;
    private String country;

}
