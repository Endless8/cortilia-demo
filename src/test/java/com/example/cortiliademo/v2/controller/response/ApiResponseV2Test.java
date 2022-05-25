package com.example.cortiliademo.v2.controller.response;

import com.example.cortiliademo.v2.model.Address;
import com.example.cortiliademo.v2.model.Alumnus;
import com.example.cortiliademo.v2.model.Education;
import com.example.cortiliademo.v2.model.education.Master;
import com.example.cortiliademo.v2.model.education.Phd;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ApiResponseV2Test {

    private ApiResponseV2 apiResponseV2;

    @Test
    void tryPopulateApiResponse() throws IOException {
        // Given an existing response from service
        Address address = new Address("Via Cicerone", "23", "Italy");
        Master master = new Master("Università di Camerino", "2022");
        Phd phd = new Phd("Università di Camerino", "2019");
        Education education = new Education(master, phd);
        Alumnus alumnus = new Alumnus(
                "Edoardo",
                List.of(address),
                education
        );
        apiResponseV2 = new ApiResponseV2(List.of(alumnus), 0, 1);

        // When ApiResponse try to populate the response
        apiResponseV2.populateApiResponse();

        // Then ApiResponse returns the response body as json string
        String expectedResponseBody = getResponseBody("data/tryPopulateApiResponseV2.json");
        String actualResponseBody = apiResponseV2.getResponseBody();
        assertThat(actualResponseBody.equals(expectedResponseBody)).isTrue();
    }

    private String getResponseBody(String filePath) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(filePath);
        byte[] byteArray = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
        String requestBody = new String(byteArray, StandardCharsets.UTF_8);
        return requestBody;
    }
}