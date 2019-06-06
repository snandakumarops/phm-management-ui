package com.redhat.makerchecker.ui;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadSvg {

    public static void loadSvg(String processInstanceId, String artifactName) throws Exception {
        String output = "";

        URL url = new URL("http://localhost:8080/kie-server/services/rest/server" +
                "/containers/"+artifactName+"/images/processes/instances/"+processInstanceId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        conn.setRequestProperty("Content-Type", "application/json");
        //Switch this out with the user authentication for BC
        conn.setRequestProperty("Authorization", "Basic cmhwYW1BZG1pbjpMb3N0LTIwMTg=");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String newValue = "";
        File file = new File("/Users/sadhananandakumar/Documents/Demos/DocumentGenerator_new/" +
                "MakerCheckerUI/src/main/resources/webroot/process-diagram.svg");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        while ((newValue=br.readLine()) != null) {
            fileOutputStream.write(newValue.getBytes());
        }


    }

}
