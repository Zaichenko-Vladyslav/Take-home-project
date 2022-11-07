package com.spatial.laser.service;

import com.spatial.laser.model.House;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class HouseService {

    // Return list of objects House from table (getting from query)
    public List<House> houseList(String placekeyURL,
                                 String placekeyAPIKey,
                                 String placekeyContentType,
                                 String dbURL,
                                 String dbUsername,
                                 String dbPassword,
                                 String query) throws SQLException, IOException {

        PlacekeyService placekeyService = new PlacekeyService();

        // Connection to postgreSQL database
        Connection connection = DriverManager.getConnection(dbURL,
                dbUsername,
                dbPassword);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        // Create empty list of objects House
        List<House> listOfHouses = new ArrayList<>();

        // Get instances of class House
        while (resultSet.next()) {
            House house = new House(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    // Generate placekey for each row
                    placekeyService.generatePlacekey(placekeyURL,
                            placekeyAPIKey,
                            placekeyContentType,
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3)));
            // add new item to List
            listOfHouses.add(house);
        }
        return listOfHouses;
    }

    // Remove duplicate items from list2 if list1 contains this items
    public List<House> listWithoutDuplicates(List<House> list1, List<House> list2) {

        StandardAddressService standardAddressService = new StandardAddressService();

        List<House> listResult = list2;

        for (int i = 0; i < list2.size(); i++) {
            for (int j = 0; j < list1.size(); j++) {
                // In case placekey is "Invalid address", impossible to completely remove all duplicates
                // as field "address" can be written in many different ways, but actually be the same address
                if (list2.get(i).getPlacekey().equals("Invalid address")) {
                    if (list2.get(i).getAddress().equals(list1.get(j).getAddress()) &&
                            list2.get(i).getCity().equals(list1.get(j).getCity()) &&
                            list2.get(i).getState().equals(list1.get(j).getState())) {
                        listResult.remove(list2.get(i));
                        break;
                    }
                    if (standardAddressService.standardizeAddress(list2.get(i).getAddress()).equals(
                            standardAddressService.standardizeAddress(list1.get(j).getAddress())) &&
                            list2.get(i).getCity().equals(list1.get(j).getCity()) &&
                            list2.get(i).getState().equals(list1.get(j).getState())) {
                        listResult.remove(list2.get(i));
                        break;
                    }
                } else {
                    if (list2.get(i).getPlacekey().equals(list1.get(j).getPlacekey())) {
                        listResult.remove(list2.get(i));
                        break;
                    }
                }
            }
        }

        return listResult;
    }
}
