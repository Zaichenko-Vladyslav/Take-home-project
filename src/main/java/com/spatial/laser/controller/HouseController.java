package com.spatial.laser.controller;

import com.spatial.laser.service.HouseService;
import com.spatial.laser.model.House;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://main.d3goeq0pmrk8w1.amplifyapp.com"})
@RestController
@RequestMapping("/house")
public class HouseController {

    @Value("${spring.datasource.url}")
    private String dbURL;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${placekey.url}")
    private String placekeyURL;

    @Value("${placekey.apikey}")
    private String placekeyAPIKey;

    @Value("${placekey.content.type}")
    private String placekeyContentType;

    private String queryTableA = "select * from table_a";
    private String queryTableB = "select * from table_b";

    HouseService houseService = new HouseService();

    @GetMapping("/get-table-a")
    public List<House> getTableA() throws IOException, SQLException {
        return houseService.houseList(placekeyURL,
                placekeyAPIKey,
                placekeyContentType,
                dbURL,
                dbUsername,
                dbPassword,
                queryTableA);
    }

    @GetMapping("/get-table-b")
    public List<House> getTableB() throws IOException, SQLException {
        return houseService.houseList(placekeyURL,
                placekeyAPIKey,
                placekeyContentType,
                dbURL,
                dbUsername,
                dbPassword,
                queryTableB);
    }

    @GetMapping("/get-table-b-without-duplicates")
    public List<House> getTableBWithoutDuplicates() throws IOException, SQLException {
        return houseService.listWithoutDuplicates(houseService.houseList(placekeyURL,
                placekeyAPIKey,
                placekeyContentType,
                dbURL,
                dbUsername,
                dbPassword,
                queryTableA),
                houseService.houseList(placekeyURL,
                        placekeyAPIKey,
                        placekeyContentType,
                        dbURL,
                        dbUsername,
                        dbPassword,
                        queryTableB));
    }
}