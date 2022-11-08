package com.spatial.laser.service;

import com.spatial.laser.model.Houses;
import com.spatial.laser.repository.HousesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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

    @Override
    public List<Houses> getTableBWithoutDuplicatesAlternative(String placekeyURL,
                                String placekeyAPIKey,
                                String placekeyContentType) throws IOException {

        // Get two initial lists
        List<Houses> listA = housesRepository.getTableA();
        List<Houses> listB = housesRepository.getTableB();

        // Get list A + list B
        List<Houses> listAB = housesRepository.getTableAAndB();

        // Initialise list result based on table B (without duplicates)
        List<Houses> listResult = new ArrayList<>();

        // if table_b contain item and table_a does not contain exactly the same item, add item to listResult
        // in the same time check if this item already in the listResult
        for (Houses item : listAB) {
            if (listB.contains(item) && !listA.contains(item) && !listResult.contains(item)) {
                listResult.add(item);
            }
        }

        // some items can have the same placekey, but in the same time have a different addresses
        // checking listResult for duplicates by additional parameter - placekey
        outerLoop: for (Houses listResultItem : listResult) {

            String placekeyForItemInResultList = placekeyService.generatePlacekey(
                    placekeyURL,
                    placekeyAPIKey,
                    placekeyContentType,
                    listResultItem.getId().getAddress(),
                    listResultItem.getId().getCity(),
                    listResultItem.getId().getState());

            // Firstly, checking items with placekey not equals "Invalid address"
            // Secondly checking items with placekey equals "Invalid address"
            if (!placekeyForItemInResultList.equals("Invalid address")) {
                for (Houses listAItem : listA) {
                    String placekeyForItemInListA = placekeyService.generatePlacekey(
                            placekeyURL,
                            placekeyAPIKey,
                            placekeyContentType,
                            listAItem.getId().getAddress(),
                            listAItem.getId().getCity(),
                            listAItem.getId().getState());
                    if (placekeyForItemInResultList.equals(placekeyForItemInListA)) {
                        listResult.remove(listResultItem);
                        break outerLoop;
                    }
                }
            } else {
                // It is possible to find a few more duplicates by standardizing address field
                for (Houses listAItem : listA) {
                    if (standardAddressService.standardizeAddress(listResultItem.getId().getAddress())
                            .equals(standardAddressService.standardizeAddress(listAItem.getId().getAddress()))
                    && listResultItem.getId().getCity().equals(listAItem.getId().getCity())
                    && listResultItem.getId().getState().equals(listAItem.getId().getState())) {
                        listResult.remove(listResultItem);
                        break outerLoop;
                    }
                }
            }
        }

        return listResult;
    }
}