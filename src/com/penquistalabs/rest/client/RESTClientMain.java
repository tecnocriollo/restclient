package com.penquistalabs.rest.client;

import java.io.IOException;

/**
 * Created by psanc on 16-04-2016.
 */
public class RESTClientMain {

    public static void main(String[] args) throws IOException {
        String serverUrl = "http://localhost:8080";

        RESTClient restClient = new RESTClient(serverUrl);

        RESTClientJSONArrayResponse response = restClient.getJsonArray("stations/geoloc");

        for(int i=0; i<response.jsondata.length();i++){
            System.out.println(response.jsondata.getJSONObject(i));
        }
    }
}
