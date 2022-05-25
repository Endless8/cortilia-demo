package com.example.cortiliademo.v1.model;

import com.example.cortiliademo.v1.model.education.Master;
import com.example.cortiliademo.v1.model.education.Phd;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Education {

    private Master master;
    private Phd phd;

    public Education(Master master) {
        this.master = master;
    }

    public Education(Phd phd) {
        this.phd = phd;
    }

}
