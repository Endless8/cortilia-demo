package com.example.cortiliademo.v2.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Alumnus {

    @Id
    private String id;
    private String name;
    private List<Address> address;
    private Education education;

    public Alumnus(String name, List<Address> address, Education education) {
        this.name = name;
        this.address = address;
        this.education = education;
    }
}
