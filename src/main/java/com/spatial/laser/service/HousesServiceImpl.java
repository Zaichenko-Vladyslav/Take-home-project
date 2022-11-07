package com.spatial.laser.service;

import com.spatial.laser.model.Houses;
import com.spatial.laser.repository.HousesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class HousesServiceImpl implements HousesService {

    @Autowired
    StandardAddressService standardAddressService;

    @Autowired
    PlacekeyService placekeyService;

    @Autowired
    private HousesRepository housesRepository;

    @Override
    public List<Houses> getTableA() {
        return housesRepository.getTableA();
    }

    @Override
    public List<Houses> getTableB() {
        return housesRepository.getTableB();
    }

    public List<Houses> getTableBWithoutDuplicates(String placekeyURL,
                                                   String placekeyAPIKey,
                                                   String placekeyContentType) throws IOException {

        List<Houses> listTableA = housesRepository.getTableA();
        List<Houses> listTableB = housesRepository.getTableB();

        for (int i = 0; i < listTableB.size(); i++) {
            String placekey2 = placekeyService.generatePlacekey(placekeyURL,
                    placekeyAPIKey,
                    placekeyContentType,
                    listTableB.get(i).getId().getAddress(),
                    listTableB.get(i).getId().getCity(),
                    listTableB.get(i).getId().getState());
            for (int j = 0; j < listTableA.size(); j++) {
                String placekey1 = placekeyService.generatePlacekey(placekeyURL,
                        placekeyAPIKey,
                        placekeyContentType,
                        listTableA.get(j).getId().getAddress(),
                        listTableA.get(j).getId().getCity(),
                        listTableA.get(j).getId().getState());

                // In case placekey is "Invalid address", impossible to completely remove all duplicates
                // as field "address" can be written in many different ways, but actually be the same address
                if (placekey2.equals("Invalid address")) {
                    if (listTableB.get(i).getId().getAddress().equals(listTableA.get(j).getId().getAddress()) &&
                            listTableB.get(i).getId().getCity().equals(listTableA.get(j).getId().getCity()) &&
                            listTableB.get(i).getId().getState().equals(listTableA.get(j).getId().getState())) {
                        listTableB.remove(i);
                        break;
                    }
                    // Same statement as upper, but with standardizing address field (can cover a few more rows)
                    if (standardAddressService.standardizeAddress(listTableB.get(i).getId().getAddress()).equals(
                            standardAddressService.standardizeAddress(listTableA.get(j).getId().getAddress())) &&
                            listTableB.get(i).getId().getCity().equals(listTableA.get(j).getId().getCity()) &&
                            listTableB.get(i).getId().getState().equals(listTableA.get(j).getId().getState())) {
                        listTableB.remove(i);
                        break;
                    }
                } else {
                    if (placekey2.equals(placekey1)) {
                        listTableB.remove(i);
                        break;
                    }
                }
            }
        }

        return listTableB;
    }
}
