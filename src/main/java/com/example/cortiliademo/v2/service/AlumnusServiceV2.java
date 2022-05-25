package com.example.cortiliademo.v2.service;

import com.example.cortiliademo.v2.model.Address;
import com.example.cortiliademo.v2.model.Alumnus;
import com.example.cortiliademo.v2.repository.AlumnusRepositoryV2;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AlumnusServiceV2 {

    private final AlumnusRepositoryV2 alumnusRepositoryV2;
    private final Gson gson = new Gson();

    public Page<Alumnus> getAlumniByName(String name, String page, String education) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, 2);
        Page<Alumnus> alumniPages;
        if (education == null) {
            alumniPages = alumnusRepositoryV2.findAlumniByName(name, pageable);
        } else {
            switch (education.trim()) {
                case "master" -> alumniPages = alumnusRepositoryV2.findAlumniByEducation_MasterNotNull(pageable);
                case "phd" -> alumniPages = alumnusRepositoryV2.findAlumniByEducation_PhdNotNull(pageable);
                case "" -> alumniPages = alumnusRepositoryV2.customFindAlumniByEducationEmpty(pageable);
                default -> alumniPages = Page.empty(pageable);
            }
        }
        return alumniPages;
    }

    public void saveAlumnus(String requestBody) {
        Alumnus alumnus = gson.fromJson(requestBody, Alumnus.class);
        List<Address> addressList = alumnus.getAddress();
        for (Address address : addressList)
            if (address.getStreet().isBlank() || address.getNumber().isBlank() || address.getCountry().isBlank())
                throw new IllegalArgumentException("The inserted address can't contain empty fields!");
            else if (!address.getStreet().matches("[a-zA-Z\\s]+"))
                throw new IllegalArgumentException("The address name can only contains alphabetic characters!");
            else if (!address.getNumber().matches("\\d+"))
                throw new IllegalArgumentException("The address number can only contains numeric characters!");
        alumnusRepositoryV2.save(alumnus);
    }
}
