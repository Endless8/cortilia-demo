package com.example.cortiliademo.v2.controller;

import com.example.cortiliademo.v2.model.Address;
import com.example.cortiliademo.v2.model.Alumnus;
import com.example.cortiliademo.v2.model.Education;
import com.example.cortiliademo.v2.model.education.Master;
import com.example.cortiliademo.v2.model.education.Phd;
import com.example.cortiliademo.v2.service.AlumnusServiceV2;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class AlumnusControllerV2Test {

    @Mock
    private AlumnusServiceV2 alumnusServiceV2Test;
    private AlumnusControllerV2 alumnusControllerV2;
    private Gson gson;

    @BeforeEach
    void setUp() {
        alumnusControllerV2 = new AlumnusControllerV2(alumnusServiceV2Test);
    }

    @Test
    void tryToGetExistingAlumnusByName() throws IOException {
        // Given an existing alumnus
        String nameString = "Edoardo";
        String pageString = "1";
        String educationString = "master";
        Address address = new Address("Via Cicerone", "23", "Italy");
        Master master = new Master("Università di Camerino", "2022");
        Phd phd = new Phd("Università di Camerino", "2019");
        Education education = new Education(master, phd);
        Alumnus alumnus = new Alumnus(
                "Edoardo",
                List.of(address),
                education
        );
        Pageable pageable = PageRequest.of(Integer.parseInt(pageString) - 1, 2);
        given(alumnusServiceV2Test.getAlumniByName(nameString, pageString, educationString))
                .willReturn(new PageImpl<Alumnus>(List.of(alumnus), pageable, 1));

        // When controller tries to get the existing alumnus
        ResponseEntity<String> actualResponse = alumnusControllerV2.getAlumnusByName(nameString, pageString, educationString);

        // Then the controller returns an OK response with a response body
        String expectedResponseBody = getJsonStringFromFile("data/tryToGetExistingAlumnusByNameV2.json");
        ResponseEntity<String> expectedResponse = new ResponseEntity<>(expectedResponseBody, HttpStatus.OK);
        assertThat(actualResponse.equals(expectedResponse)).isTrue();
    }

    @Test
    void tryToGetNonExistingAlumnusByName() throws IOException {
        // Given a non existing alumnus
        String nameString = "Edoardo";
        String pageString = "1";
        String educationString = "master";
        Pageable pageable = PageRequest.of(Integer.parseInt(pageString) - 1, 2);
        given(alumnusServiceV2Test.getAlumniByName(nameString, pageString, educationString))
                .willReturn(new PageImpl<Alumnus>(Collections.emptyList(), pageable, 1));

        // When controller tries to get the existing alumnus
        ResponseEntity<String> actualResponse = alumnusControllerV2.getAlumnusByName(nameString, pageString, educationString);

        // Then the controller returns a NO CONTENT response
        ResponseEntity<String> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        assertThat(actualResponse.equals(expectedResponse)).isTrue();
    }

    @Test
    void tryToSaveValidAlumnusJson() throws IOException {
        // Given a valid alumnus json as request body
        String requestBody = getJsonStringFromFile("data/trySaveAlumnus.json");

        // When the controller tries to save alumnus
        ResponseEntity<String> actualResponse = alumnusControllerV2.saveAlumnus(requestBody);

        // Then the controller returns a CREATED response
        ResponseEntity<String> expectedResponse = new ResponseEntity<>(HttpStatus.CREATED);
        assertThat(actualResponse.equals(expectedResponse)).isTrue();
    }

    @Test
    void tryToSaveInvalidAlumnusJson() throws IOException {
        // Given an invalid alumnus json as request body
        String requestBody = getJsonStringFromFile("data/trySaveInvalidAlumnus.json");
        doThrow(new IllegalArgumentException("The address name can only contains alphabetic characters!")).when(alumnusServiceV2Test).saveAlumnus(requestBody);

        // When the controller tries to save alumnus
        ResponseEntity<String> actualResponse = alumnusControllerV2.saveAlumnus(requestBody);

        // Then the controller returns a CREATED response
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("The address name can only contains alphabetic characters!", HttpStatus.BAD_REQUEST);
        assertThat(actualResponse.equals(expectedResponse)).isTrue();
    }

    private String getJsonStringFromFile(String filePath) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(filePath);
        byte[] byteArray = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
        String requestBody = new String(byteArray, StandardCharsets.UTF_8);
        gson = new Gson();
        return requestBody;
    }
}