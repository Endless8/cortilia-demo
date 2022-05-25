package com.example.cortiliademo.v1.service;

import com.example.cortiliademo.v1.model.Alumnus;
import com.example.cortiliademo.v1.repository.AlumnusRepository;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@AllArgsConstructor
@Service
public class AlumnusService {

    private final AlumnusRepository alumnusRepository;
    private final Gson gson = new Gson();

    public List<Alumnus> getAlumniByName(String name) {
        return alumnusRepository.findAlumniByName(name);
    }

    public void saveAlumnus(String requestBody) {
        Alumnus alumnus = gson.fromJson(requestBody, Alumnus.class);
        alumnusRepository.save(alumnus);
    }
}
