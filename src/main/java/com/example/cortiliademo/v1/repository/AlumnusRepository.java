package com.example.cortiliademo.v1.repository;

import com.example.cortiliademo.v1.model.Alumnus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AlumnusRepository extends MongoRepository<Alumnus, String> {

    List<Alumnus> findAlumniByName(String name);

}
