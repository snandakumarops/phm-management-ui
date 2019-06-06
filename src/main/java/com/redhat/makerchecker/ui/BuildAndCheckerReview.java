package com.redhat.makerchecker.ui;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BuildAndCheckerReview {

    public static void initiateMakerCheckerWorkflow(String artifactName, String comments) throws Exception {
        String output = "";

        URL url = new URL("http://localhost:8080/kie-server/services/rest/server/containers/MakerCheckerNewProject_1.0.0/" +
                "processes/MakerCheckerNewProject.TestNewProcess/instances");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        //Switch this out with the user authentication for BC
        conn.setRequestProperty("Authorization", "Basic cmhwYW1BZG1pbjpMb3N0LTIwMTg=");

        String input = "{\"artifactName\":\"" + artifactName + "\", " +
                "\"authorComments\":\""+comments+"\"}";

        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();



        System.out.println(conn.getResponseCode()+conn.getResponseMessage());
    }




}
