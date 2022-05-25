package com.example.cortiliademo.v1.repository;

import com.example.cortiliademo.v1.model.Address;
import com.example.cortiliademo.v1.model.Alumnus;
import com.example.cortiliademo.v1.model.Education;
import com.example.cortiliademo.v1.model.education.Master;
import com.example.cortiliademo.v1.model.education.Phd;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class AlumnusRepositoryTest {

    @Autowired
    private AlumnusRepository alumnusRepositoryTest;

    @Test
    void checkIfAlumniExistsFromEmail() {
        // Given an existing alumnus
        Address address = new Address("Via Cicerone", "23", "Italy");
        Master master = new Master("Università di Camerino", "2022");
        Phd phd = new Phd("Università di Camerino", "2019");
        Education education = new Education(master, phd);
        Alumnus alumnus = new Alumnus(
                "Edoardo",
                List.of(address),
                education
        );
        alumnusRepositoryTest.save(alumnus);

        // When repository finds alumni by name
        List<Alumnus> alumniList = alumnusRepositoryTest.findAlumniByName(alumnus.getName());

        // Then repository fetch the existing alumnus
        int alumniCount = alumniList.size();
        assertThat(alumniCount).isEqualTo(1);
        Alumnus fetchedAlumnus = alumniList.get(0);
        assertThat(alumnus).isEqualTo(fetchedAlumnus);
    }

    @AfterEach
    void tearDown() {
        alumnusRepositoryTest.deleteAll();
    }

}