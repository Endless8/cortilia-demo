package com.example.cortiliademo.v2.repository;

import com.example.cortiliademo.v2.model.Address;
import com.example.cortiliademo.v2.model.Alumnus;
import com.example.cortiliademo.v2.model.Education;
import com.example.cortiliademo.v2.model.education.Master;
import com.example.cortiliademo.v2.model.education.Phd;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class AlumnusRepositoryV2Test {

    @Autowired
    private AlumnusRepositoryV2 alumnusRepositoryV2Test;

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
        alumnusRepositoryV2Test.save(alumnus);

        // When repository finds alumni by name
        Pageable pageable = PageRequest.of(0, 2);
        Page<Alumnus> alumniByName = alumnusRepositoryV2Test.findAlumniByName(alumnus.getName(), pageable);

        // Then repository fetch the existing alumnus
        List<Alumnus> alumniList = alumniByName.getContent();
        int alumniCount = alumniList.size();
        assertThat(alumniCount).isEqualTo(1);
        Alumnus fetchedAlumnus = alumniList.get(0);
        assertThat(alumnus).isEqualTo(fetchedAlumnus);
    }

    @Test
    void findAlumniByEducation_MasterNotNull() {
        // Given an existing alumnus with a Master education
        Address address = new Address("Via Cicerone", "23", "Italy");
        Master master = new Master("Università di Camerino", "2022");
        Education education = new Education(master);
        Alumnus alumnus = new Alumnus(
                "Edoardo",
                List.of(address),
                education
        );
        alumnusRepositoryV2Test.save(alumnus);

        // When repository finds alumni by a master education
        Pageable pageable = PageRequest.of(0, 2);
        Page<Alumnus> alumniByName = alumnusRepositoryV2Test.findAlumniByEducation_MasterNotNull(pageable);

        // Then repository fetches the existing alumnus with a Master education
        List<Alumnus> alumniList = alumniByName.getContent();
        int alumniCount = alumniList.size();
        assertThat(alumniCount).isEqualTo(1);
        Alumnus fetchedAlumnus = alumniList.get(0);
        assertThat(alumnus).isEqualTo(fetchedAlumnus);
    }

    @Test
    void findAlumniByEducation_PhdNotNull() {
        // Given an existing alumnus with a Master education
        Address address = new Address("Via Cicerone", "23", "Italy");
        Phd phd = new Phd("Università di Camerino", "2019");
        Education education = new Education(phd);
        Alumnus alumnus = new Alumnus(
                "Edoardo",
                List.of(address),
                education
        );
        alumnusRepositoryV2Test.save(alumnus);

        // When repository finds alumni by a master education
        Pageable pageable = PageRequest.of(0, 2);
        Page<Alumnus> alumniByName = alumnusRepositoryV2Test.findAlumniByEducation_PhdNotNull(pageable);

        // Then repository fetches the existing alumnus with a Master education
        List<Alumnus> alumniList = alumniByName.getContent();
        int alumniCount = alumniList.size();
        assertThat(alumniCount).isEqualTo(1);
        Alumnus fetchedAlumnus = alumniList.get(0);
        assertThat(alumnus).isEqualTo(fetchedAlumnus);
    }

    @Test
    void customFindAlumniByEducationEmpty() {
        // Given an existing alumnus with a Master education
        Address address = new Address("Via Cicerone", "23", "Italy");
        Education education = new Education();
        Alumnus alumnus = new Alumnus(
                "Edoardo",
                List.of(address),
                education
        );
        alumnusRepositoryV2Test.save(alumnus);

        // When repository finds alumni by a master education
        Pageable pageable = PageRequest.of(0, 2);
        Page<Alumnus> alumniByName = alumnusRepositoryV2Test.customFindAlumniByEducationEmpty(pageable);

        // Then repository fetches the existing alumnus with a Master education
        List<Alumnus> alumniList = alumniByName.getContent();
        int alumniCount = alumniList.size();
        assertThat(alumniCount).isEqualTo(1);
        Alumnus fetchedAlumnus = alumniList.get(0);
        assertThat(alumnus).isEqualTo(fetchedAlumnus);
    }

    @AfterEach
    void tearDown() {
        alumnusRepositoryV2Test.deleteAll();
    }

}