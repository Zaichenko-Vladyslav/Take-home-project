package com.spatial.laser.controller;

import com.spatial.laser.model.Houses;
import com.spatial.laser.service.HousesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://main.d3goeq0pmrk8w1.amplifyapp.com"})
@RestController
@RequestMapping("/houses")
public class HousesController {

    @Value("${placekey.url}")
    private String placekeyURL;

    @Value("${placekey.apikey}")
    private String placekeyAPIKey;

    @Value("${placekey.content.type}")
    private String placekeyContentType;

    @Autowired
    private HousesService housesService;

    @GetMapping("/get-table-a")
    public List<Houses> getTableA() {
        return housesService.getTableA();
    }

    @GetMapping("/get-table-b")
    public List<Houses> getTableB() {
        return housesService.getTableB();
    }

    @GetMapping("/get-table-b-without-duplicates")
    public List<Houses> getTableBWithoutDuplicates() throws IOException {
        return housesService.getTableBWithoutDuplicates(placekeyURL, placekeyAPIKey, placekeyContentType);
    }
}
