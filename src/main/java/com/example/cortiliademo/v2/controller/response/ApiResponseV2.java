package com.example.cortiliademo.v2.controller.response;

import com.example.cortiliademo.v2.model.Alumnus;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApiResponseV2 {

    private List<Alumnus> alumniList;
    private final int currentPage;
    private final int totalPages;
    private Gson gson;
    private String responseBody;

    public ApiResponseV2(List<Alumnus> alumniList, int currentPage, int totalPages) {
        this.alumniList = alumniList;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        gson = new Gson();
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
        bodyResponseMap.put("currentPage", currentPage + 1);
        bodyResponseMap.put("totalPages", totalPages);
        responseBody = gson.toJson(bodyResponseMap);
    }

    public List<Alumnus> getAlumniList() {
        return alumniList;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
