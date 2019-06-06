package com.redhat.makerchecker.ui;

import com.RulesFired;
import com.google.gson.Gson;
import com.google.gson.internal.StringMap;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TaskHandlerUtil {

      public static List<TaskSummaryObject> fetchDashboardData() throws Exception {
          String output = "";
          TaskSummaryObjectList taskSummaryObjectList = new TaskSummaryObjectList();
          List<TaskSummaryObject> taskSummaryObjects = new ArrayList<>();

          try {
              File file = new File("/Users/sadhananandakumar/Documents/Demos/DocumentGenerator_new/" +
                      "DocumentGeneratorUI/src/main/resources/webroot/process-diagram.svg");
              file.delete();

              TaskSummaryObject taskSummaryObject = null;
              URL url = new URL("http://localhost:8080/kie-server/services/rest/server/queries/tasks/instances/pot-owners?" +
                      "status=Created&status=Ready&status=Reserved&status=InProgress&user=rhpamAdmin");
              HttpURLConnection conn = (HttpURLConnection) url.openConnection();
              conn.setRequestMethod("GET");
              conn.setRequestProperty("Accept", "application/json");
              //Switch this out with the user authentication for BC
              conn.setRequestProperty("Authorization", "Basic cmhwYW1BZG1pbjpMb3N0LTIwMTg=");

              if (conn.getResponseCode() != 200) {
                  throw new RuntimeException("Failed : HTTP error code : "
                          + conn.getResponseCode());
              }

              BufferedReader br = new BufferedReader(new InputStreamReader(
                      (conn.getInputStream())));


              System.out.println("Output from Server .... \n");
              String newValue;
              while ((newValue=br.readLine()) != null) {
               output+= newValue;
              }

              Object taskSummary = new Gson().fromJson(output,Object.class);

              StringMap map = (StringMap)taskSummary;
              List<StringMap> listEntries = (List<StringMap>) ((StringMap) taskSummary).get("task-summary");
              Double d = null;
              Double processInstId = null;
              for(StringMap stringMap:listEntries) {
                  taskSummaryObject = new TaskSummaryObject();
                  d = (Double)stringMap.get("task-id");
                  processInstId = (Double)stringMap.get("task-proc-inst-id");
                  taskSummaryObject.setTaskId(String.valueOf(d.intValue()));
                  taskSummaryObject.setTaskContainerId((String)stringMap.get("task-container-id"));
                  taskSummaryObject.setSummaryOfChanges((String)stringMap.get("task-description"));
                  taskSummaryObject.setProcessInstanceId(String.valueOf(processInstId.intValue()));
                  taskSummaryObjects.add(taskSummaryObject);


              }

              taskSummaryObjectList.setTaskSummaryObjects(taskSummaryObjects);

              conn.disconnect();



          } catch (MalformedURLException e) {

              e.printStackTrace();

          } catch (IOException e) {

              e.printStackTrace();

          }


          return taskSummaryObjects;
      }

    public static List<RulesFired> fetchSimulation(String listType, String taskId) throws Exception {
        String output = "";

        List<RulesFired> taskSummaryObjects = new ArrayList<>();

        try {

            RulesFired rulesFired = null;
            URL url = new URL("http://localhost:8080/kie-server/services/rest" +
                    "/server/containers/MakerCheckerNewProject_1.0.0/processes/instances/"+taskId+"/variable/"+listType);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            //Switch this out with the user authentication for BC
            conn.setRequestProperty("Authorization", "Basic cmhwYW1BZG1pbjpMb3N0LTIwMTg=");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));


            System.out.println("Output from Server .... \n");
            String newValue;
            while ((newValue=br.readLine()) != null) {
                output+= newValue;
            }

            Object simulation = new Gson().fromJson(output,Object.class);

            ArrayList<StringMap> map = (ArrayList<StringMap>) simulation;
            Object obj = null;
            RulesFired rule = null;
            for(StringMap stringMap:map) {
                obj = stringMap.get("com.RulesFired");
                rule = new Gson().fromJson(new Gson().toJson(obj),RulesFired.class);
                taskSummaryObjects.add(rule);

            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }


        return taskSummaryObjects;
    }


    public static void approveOrReject(boolean approval, String taskId) throws Exception {
        String output = "";

        URL url = new URL("http://localhost:8080/kie-server/services/rest/server/containers/MakerCheckerNewProject_1.0.0/tasks/" + taskId + "/states/started");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Accept", "application/json");
        //Switch this out with the user authentication for BC
        conn.setRequestProperty("Authorization", "Basic cmhwYW1BZG1pbjpMb3N0LTIwMTg=");

        if (conn.getResponseCode() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }


        URL completeURL = new URL("http://localhost:8080/kie-server/services/rest/server/containers/MakerCheckerNewProject_1.0.0/tasks/" + taskId + "/states/completed");
        HttpURLConnection conn1 = (HttpURLConnection) completeURL.openConnection();
        conn1.setDoOutput(true);
        conn1.setRequestMethod("PUT");
        conn1.setRequestProperty("Content-Type", "application/json");
        conn1.setRequestProperty("accept", "application/json");

        String input = "{\"approved\":" + approval + "}";
        conn1.setRequestProperty("Authorization", "Basic cmhwYW1BZG1pbjpMb3N0LTIwMTg=");

        OutputStream os = conn1.getOutputStream();
        os.write(input.getBytes());
        os.flush();

        System.out.println(conn1.getResponseMessage());
    }







}
