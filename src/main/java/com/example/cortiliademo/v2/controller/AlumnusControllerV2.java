package com.example.cortiliademo.v2.controller;

import com.example.cortiliademo.v2.model.Alumnus;
import com.example.cortiliademo.v2.controller.response.ApiResponseV2;
import com.example.cortiliademo.v2.service.AlumnusServiceV2;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v2/ex-1/alumni")
@AllArgsConstructor
@Slf4j
public class AlumnusControllerV2 {
    private final AlumnusServiceV2 alumnusServiceV2;

    @GetMapping
    public ResponseEntity<String> getAlumnusByName(@RequestParam String name, @RequestParam String page, @RequestParam(required = false) String education) {
        log.info("Invoking GET Method {}...", Thread.currentThread().getStackTrace()[1].getMethodName());
        Page<Alumnus> alumniPages = alumnusServiceV2.getAlumniByName(name, page, education);
        List<Alumnus> alumniList = alumniPages.getContent();
        if (alumniList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else {
            int currentPage = alumniPages.getNumber();
            int totalPages = alumniPages.getTotalPages();
            ApiResponseV2 apiResponseV2 = new ApiResponseV2(alumniList, currentPage, totalPages);
            apiResponseV2.populateApiResponse();
            String responseBody = apiResponseV2.getResponseBody();
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<String> saveAlumnus(@RequestBody String requestBody) {
        log.info("Invoking POST Method {}...", Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            alumnusServiceV2.saveAlumnus(requestBody);
            log.info("Alumnus saved!");
        } catch (Exception e) {
            log.error("Error while saving alumnus", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
