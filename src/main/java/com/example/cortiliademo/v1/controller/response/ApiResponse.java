package com.example.cortiliademo.v1.controller.response;

import com.example.cortiliademo.v1.model.Alumnus;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApiResponse {

    private List<Alumnus> alumniList;
    private Gson gson = new Gson();
    private String responseBody;

    public ApiResponse(List<Alumnus> alumniList) {
        this.alumniList = alumniList;
    }

    public void populateApiResponse() {
        HashMap<String, Object> bodyResponseMap = new HashMap<>();
        List<Alumnus> noIdAlumniList = new ArrayList<>();
        alumniList.forEach(alumnus -> {
            Alumnus noIdAlumnus = new Alumnus(
                    alumnus.getName(),
                    alumnus.getAddress(),
                    alumnus.getEducation()
            );
            noIdAlumniList.add(noIdAlumnus);
        });
        bodyResponseMap.put("data", noIdAlumniList);
        bodyResponseMap.put("totalCount", noIdAlumniList.size());
        responseBody = gson.toJson(bodyResponseMap);
    }

    public List<Alumnus> getAlumniList() {
        return alumniList;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
