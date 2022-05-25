package com.example.cortiliademo.v1.model.education;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class EducationType {

    private String university;
    private String year;

}
