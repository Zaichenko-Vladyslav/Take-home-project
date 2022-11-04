package com.spatial.laser.service;

import org.springframework.stereotype.Component;

@Component
public class StandardAddressService {

    // Different streets have different abbreviations:
    // For example Street is "St", Road is "Rd", Avenue is "Ave",
    // Drive is "Dr", Boulevard is "Blvd" and Lane is "Ln",
    // Circle is "Cir", Court is "Ct" and a few more...
    public String standardizeAddress(String address) {

        String newAddress = "";

        if (address.endsWith(" St")) {
            newAddress = address.replace(" St", " Street");
        } else if (address.endsWith(" Rd")) {
            newAddress = address.replace(" Rd", " Road");
        } else if (address.endsWith(" Ave")) {
            newAddress = address.replace(" Ave", " Avenue");
        } else if (address.endsWith(" Dr")) {
            newAddress = address.replace(" Dr", " Drive");
        } else if (address.endsWith(" Blvd")) {
            newAddress = address.replace(" Blvd", " Boulevard");
        } else if (address.endsWith(" Ln")) {
            newAddress = address.replace(" Ln", " Lane");
        } else if (address.endsWith(" Cir")) {
            newAddress = address.replace(" Cir", " Circle");
        } else if (address.endsWith(" Ct")) {
            newAddress = address.replace(" Ct", " Court");
        } else {
            newAddress = address;
        }

        return newAddress;
    }
}