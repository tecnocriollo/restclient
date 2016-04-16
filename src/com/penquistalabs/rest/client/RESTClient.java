package com.penquistalabs.rest.client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by psanc on 15-04-2016.
 */
public class RESTClient {
    private String serverUrl;

    public RESTClient(String serverUrl){
        this.serverUrl = serverUrl;

    }

    public  RESTClientResponse get(String path) throws IOException {
        return this.get(path, null);
    }

    public  RESTClientResponse get(String path,HashMap<String, String> queryParams) throws  IOException {


        String url = serverUrl + '/' + path;


        if(queryParams != null){
            url = getQueryString(queryParams, url);
        }

        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        RESTClientResponse restResponse = new RESTClientResponse();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            restResponse.status = responseCode;
            restResponse.data = response.toString();
            restResponse.message = "OK";
            return restResponse;
        } else {
            restResponse.status = responseCode;
            restResponse.data = null;
            restResponse.message = "GET request not worked";
            return restResponse;
        }
    }

    private String getQueryString(HashMap<String, String> queryParams, String url) {
        StringBuilder queryStringBuilder = new StringBuilder();
        queryStringBuilder.append(url + "/?");
        Iterator it = queryParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            queryStringBuilder.append(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
            if(it.hasNext()) queryStringBuilder.append("&");
        }
        url = queryStringBuilder.toString();
        return url;
    }

    public RESTClientJSONResponse getJson(String path) throws IOException {
        return this.getJson(path, null);
    }

    public RESTClientJSONResponse getJson(String path, HashMap<String, String> queryParams) throws IOException {
        RESTClientResponse response = this.get(path,queryParams);

        RESTClientJSONResponse jsonResponse = new RESTClientJSONResponse();
        jsonResponse.status = response.status;
        jsonResponse.message = response.message;
        if(response.data != null){
            jsonResponse.jsondata = new JSONObject(response.data);
        }
        else{
            jsonResponse.jsondata = null;
        }
        return  jsonResponse;
    }

    public RESTClientJSONArrayResponse getJsonArray(String path) throws IOException {
        return this.getJsonArray(path, null);
    }

    public RESTClientJSONArrayResponse getJsonArray(String path, HashMap<String, String> queryParams) throws IOException {
        RESTClientResponse response = this.get(path,queryParams);

        RESTClientJSONArrayResponse jsonResponse = new RESTClientJSONArrayResponse();
        jsonResponse.status = response.status;
        jsonResponse.message = response.message;
        if(response.data != null){
            jsonResponse.jsondata = new JSONArray(response.data);
        }
        else{
            jsonResponse.jsondata = null;
        }
        return  jsonResponse;
    }

}
