package com.example.cortiliademo.v2.repository;

import com.example.cortiliademo.v2.model.Alumnus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AlumnusRepositoryV2 extends MongoRepository<Alumnus, String> {

    Page<Alumnus> findAlumniByName(String name, Pageable pageable);

    Page<Alumnus> findAlumniByEducation_MasterNotNull(Pageable pageable);

    Page<Alumnus> findAlumniByEducation_PhdNotNull(Pageable pageable);

    @Query("{ education: {} }")
    Page<Alumnus> customFindAlumniByEducationEmpty(Pageable pageable);

}
