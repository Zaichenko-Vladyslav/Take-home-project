package com.spatial.laser.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

@Component
public class PlacekeyService {

    // Generate placekey for single house by address, city, state
    public String generatePlacekey(String placekeyURL,
                                   String placekeyAPIKey,
                                   String placekeyContentType,
                                   String address,
                                   String city,
                                   String state) throws IOException {

        URL url = new URL(placekeyURL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestProperty("apikey", placekeyAPIKey);
        connection.setRequestProperty("Content-Type", placekeyContentType);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

        JSONObject query = new JSONObject();
        query.put("street_address", address);
        query.put("city", city);
        query.put("region", state);
        query.put("postal_code", java.util.Optional.ofNullable(null));
        query.put("iso_country_code", "US");

        JSONObject options = new JSONObject();
        options.put("strict_address_match", false);

        JSONObject body = new JSONObject();
        body.put("query", query);
        body.put("options", options);

        writer.write(String.valueOf(body));
        writer.flush();
        writer.close();

        connection.getOutputStream().close();

        // Handle error response code
        int responseCode = connection.getResponseCode();
        InputStream inputStream;
        if (200 <= responseCode && responseCode <= 299) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder response = new StringBuilder();
        String currentLine;

        while ((currentLine = in.readLine()) != null)
            response.append(currentLine);

        in.close();

        JSONObject jsonObject = new JSONObject(response.toString());

        // Not all queries return a unique placekey
        // In case placekey was not found return error message as placekey
        if (jsonObject.has("placekey")) {
            return jsonObject.get("placekey").toString();
        } else {
            return jsonObject.get("error").toString();
        }
    }
}