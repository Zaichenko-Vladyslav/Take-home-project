package com.spatial.laser.service;

import com.spatial.laser.model.Houses;

import java.io.IOException;
import java.util.List;

public interface HousesService {

    // get table A
    List<Houses> getTableA();

    // get table B
    List<Houses> getTableB();

    // get table B without duplicates
    List<Houses> getTableBWithoutDuplicates(String placekeyURL,
                                            String placekeyAPIKey,
                                            String placekeyContentType) throws IOException;
}
