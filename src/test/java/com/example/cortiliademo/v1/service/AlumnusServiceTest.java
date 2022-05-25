package com.example.cortiliademo.v1.service;

import com.example.cortiliademo.v1.model.Alumnus;
import com.example.cortiliademo.v1.repository.AlumnusRepository;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AlumnusServiceTest {

    @Mock
    private AlumnusRepository alumnusRepositoryMock;
    private AlumnusService alumnusServiceTest;
    private Gson gson;

    @BeforeEach
    void setUp() {
        alumnusServiceTest = new AlumnusService(alumnusRepositoryMock);
    }

    @Test
    void trySaveAlumnus() throws IOException {
        // Given an existing alumnus
        String requestBody = getRequestBody("data/trySaveAlumnus.json");
        Alumnus alumnus = gson.fromJson(requestBody, Alumnus.class);

        // When the service saves that alumnus
        alumnusServiceTest.saveAlumnus(requestBody);

        // Then the alumnus is saved
        ArgumentCaptor<Alumnus> alumnusArgumentCaptor = ArgumentCaptor.forClass(Alumnus.class);
        verify(alumnusRepositoryMock).save(alumnusArgumentCaptor.capture());
        Alumnus capturedAlumnus = alumnusArgumentCaptor.getValue();
        assertThat(capturedAlumnus).isEqualTo(alumnus);
    }

    @Test
    void tryGetAlumniByName() {
        // When
        String name = "Edoardo";
        alumnusServiceTest.getAlumniByName(name);

        // Then
        Pageable pageable = PageRequest.of(0, 2);
        verify(alumnusRepositoryMock).findAlumniByName(name);
    }

    private String getRequestBody(String filePath) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(filePath);
        byte[] byteArray = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
        String requestBody = new String(byteArray, StandardCharsets.UTF_8);
        gson = new Gson();
        return requestBody;
    }

}