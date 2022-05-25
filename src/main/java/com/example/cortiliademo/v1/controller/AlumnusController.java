package com.example.cortiliademo.v1.controller;

import com.example.cortiliademo.v1.controller.response.ApiResponse;
import com.example.cortiliademo.v1.model.Alumnus;
import com.example.cortiliademo.v1.service.AlumnusService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/ex-1/alumni")
@AllArgsConstructor
@Slf4j
public class AlumnusController {
    private final AlumnusService alumnusService;

    @GetMapping
    public ResponseEntity<String> getAlumnusByName(@RequestParam String name) {
        log.info("Invoking GET Method {}...", Thread.currentThread().getStackTrace()[1].getMethodName());
        List<Alumnus> alumniList = alumnusService.getAlumniByName(name);
        if (alumniList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else {
            ApiResponse apiResponse = new ApiResponse(alumniList);
            apiResponse.populateApiResponse();
            String responseBody = apiResponse.getResponseBody();
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<String> saveAlumnus(@RequestBody String requestBody) {
        log.info("Invoking POST Method {}...", Thread.currentThread().getStackTrace()[1].getMethodName());
        alumnusService.saveAlumnus(requestBody);
        log.info("Alumnus saved!");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
