package com.example.cortiliademo.v2.service;

import com.example.cortiliademo.v2.model.Alumnus;
import com.example.cortiliademo.v2.repository.AlumnusRepositoryV2;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AlumnusServiceV2Test {

    @Mock
    private AlumnusRepositoryV2 alumnusRepositoryV2Mock;
    private AlumnusServiceV2 alumnusServiceV2Test;
    private Gson gson;

    @BeforeEach
    void setUp() {
        alumnusServiceV2Test = new AlumnusServiceV2(alumnusRepositoryV2Mock);
    }

    @Test
    void trySaveAlumnus() throws IOException {
        // Given an existing alumnus
        String requestBody = getRequestBody("data/trySaveAlumnus.json");
        Alumnus alumnus = gson.fromJson(requestBody, Alumnus.class);

        // When the service saves that alumnus
        alumnusServiceV2Test.saveAlumnus(requestBody);

        // Then the alumnus is saved
        ArgumentCaptor<Alumnus> alumnusArgumentCaptor = ArgumentCaptor.forClass(Alumnus.class);
        verify(alumnusRepositoryV2Mock).save(alumnusArgumentCaptor.capture());
        Alumnus capturedAlumnus = alumnusArgumentCaptor.getValue();
        assertThat(capturedAlumnus).isEqualTo(alumnus);
    }

    @Test
    void trySaveAlumnusWithEmptyAddressField() throws IOException {
        // Given an existing alumnus with empty address field
        String requestBody = getRequestBody("data/trySaveAlumnusWithEmptyAddressField.json");

        // When the service saves that alumnus
        // Then the service throws an exception
        assertThatThrownBy(() -> alumnusServiceV2Test.saveAlumnus(requestBody))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The inserted address can't contain empty fields!");
    }

    @Test
    void trySaveAlumnusStreetWithNumericValues() throws IOException {
        // Given an existing alumnus with empty address field
        String requestBody = getRequestBody("data/trySaveAlumnusWithStreetWithNumericValues.json");

        // When the service saves that alumnus
        // Then the service throws an exception
        assertThatThrownBy(() -> alumnusServiceV2Test.saveAlumnus(requestBody))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The address name can only contains alphabetic characters!");
    }

    @Test
    void trySaveAlumnusNumberWithAlphabeticValues() throws IOException {
        // Given an existing alumnus with empty address field
        String requestBody = getRequestBody("data/trySaveAlumnusNumberWithAlphabeticValues.json");

        // When the service saves that alumnus
        // Then the service throws an exception
        assertThatThrownBy(() -> alumnusServiceV2Test.saveAlumnus(requestBody))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The address number can only contains numeric characters!");
    }

    @Test
    void tryGetAlumniByName() {
        // When
        String name = "Edoardo";
        alumnusServiceV2Test.getAlumniByName(name, "1", null);

        // Then
        Pageable pageable = PageRequest.of(0, 2);
        verify(alumnusRepositoryV2Mock).findAlumniByName(name, pageable);
    }

    @Test
    void tryGetAlumniByNameAndMasterEducation() {
        // When
        String name = "Edoardo";
        alumnusServiceV2Test.getAlumniByName(name, "1", "master");

        // Then
        Pageable pageable = PageRequest.of(0, 2);
        verify(alumnusRepositoryV2Mock).findAlumniByEducation_MasterNotNull(pageable);
    }

    @Test
    void tryGetAlumniByNameAndPhdEducation() {
        // When
        String name = "Edoardo";
        alumnusServiceV2Test.getAlumniByName(name, "1", "phd");

        // Then
        Pageable pageable = PageRequest.of(0, 2);
        verify(alumnusRepositoryV2Mock).findAlumniByEducation_PhdNotNull(pageable);
    }

    @Test
    void tryGetAlumniByNameAndEmptyEducation() {
        // When
        String name = "Edoardo";
        alumnusServiceV2Test.getAlumniByName(name, "1", "");

        // Then
        Pageable pageable = PageRequest.of(0, 2);
        verify(alumnusRepositoryV2Mock).customFindAlumniByEducationEmpty(pageable);
    }

    @Test
    void tryGetAlumniByNameAndNonExistingEducation() {
        // When
        String name = "Edoardo";
        alumnusServiceV2Test.getAlumniByName(name, "1", "degree");
    }

    private String getRequestBody(String filePath) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(filePath);
        byte[] byteArray = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
        String requestBody = new String(byteArray, StandardCharsets.UTF_8);
        gson = new Gson();
        return requestBody;
    }

}