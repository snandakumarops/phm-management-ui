package com.redhat.phmprocess.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.StringMap;
import com.health_insurance.phm_model.Response;
import com.health_insurance.phm_model.Task;
import org.json.JSONObject;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskHandlerUtil {

    static String bcUrl = "http://localhost:8080/kie-server/services/rest";


    static Map<String,String> authMap = new HashMap<>();

    static {
        authMap.put("Peter","Basic UGV0ZXI6cmVkaGF0cGFtMSE=");
        authMap.put("Robert","Basic Um9iZXJ0OnJlZGhhdHBhbTEh");
        authMap.put("Charlie","Basic Q2hhcmxpZTpyZWRoYXRwYW0xIQ==");
        authMap.put("Mary","Basic TWFyeTpyZWRoYXRwYW0xIQ==");
    }

      public static List<TaskSummaryObject> fetchDashboardData(String userId) throws Exception {
        System.out.println("bcurl"+bcUrl);
          String output = "";
          TaskSummaryObjectList taskSummaryObjectList = new TaskSummaryObjectList();
          List<TaskSummaryObject> taskSummaryObjects = new ArrayList<>();

          try {
              File file = new File("process-diagram.svg");
              file.delete();

              TaskSummaryObject taskSummaryObject = null;
              URL url = new URL(bcUrl+"/server/queries/tasks/instances/pot-owners?user="+userId+"Peter&status=Reserved");
              HttpURLConnection conn = (HttpURLConnection) url.openConnection();
              conn.setRequestMethod("GET");
              conn.setRequestProperty("Accept", "application/json");
              //Switch this out with the user authentication for BC
              conn.setRequestProperty("Authorization", authMap.get(userId));

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
                  taskSummaryObject.setOwner((String)stringMap.get("task-actual-owner"));
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


    public static Map getTaskSummary(String taskId, String auth) throws Exception {
        String output = "";



        try {


            URL url = new URL(bcUrl +
                    "/server/containers/PHM-Processes/processes/instances/" + taskId + "/variable/" + "sData");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            //Switch this out with the user authentication for BC
            conn.setRequestProperty("Authorization", auth);

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));


            System.out.println("Output from Server .... \n");
            String newValue;
            output = "";
            while ((newValue = br.readLine()) != null) {
                output += newValue;
            }

            Map response =new Gson().fromJson(output.toString(), Map.class);
            Map responseMap = (Map) response.get("com.health_insurance.phm_model.Response");
            Map taskMap = (Map) responseMap.get("task");
//            String json = new Gson().toJson(taskMap);
            String gsonString = new Gson().toJson(taskMap.get("com.health_insurance.phm_model.Task"));
            Map taskObj = new Gson().fromJson(gsonString,Map.class);


            return taskObj;




        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return null;
    }


        public static void approveOrReject(String taskId, String user, String json, String close, String processInstanceId) throws Exception {
        String output = "";

        URL url = new URL(bcUrl+"/server/containers/PHM-Processes/tasks/" + taskId + "/states/started");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Accept", "application/json");
        //Switch this out with the user authentication for BC
        conn.setRequestProperty("Authorization", authMap.get(user));

        if (conn.getResponseCode() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }


        URL completeURL = new URL(bcUrl+"/server/containers/PHM-Processes/tasks/" + taskId + "/states/completed");
        HttpURLConnection conn1 = (HttpURLConnection) completeURL.openConnection();
        conn1.setDoOutput(true);
        conn1.setRequestMethod("PUT");
        conn1.setRequestProperty("Content-Type", "application/json");
        conn1.setRequestProperty("accept", "application/json");


        conn1.setRequestProperty("Authorization", authMap.get(user));

        OutputStream os = conn1.getOutputStream();
        os.write(json.getBytes());
        os.flush();

        System.out.println(conn1.getResponseMessage());

        if(close.equals("HARD")) {
            URL hardCloseUrl = new URL(bcUrl+"/server/containers/PHM-Processes/processes/instances/"+processInstanceId+"/signal/hard_close");
            HttpURLConnection harClseconn = (HttpURLConnection) hardCloseUrl.openConnection();
            harClseconn.setDoOutput(true);
            harClseconn.setRequestMethod("POST");
            harClseconn.setRequestProperty("Content-Type", "application/json");
            harClseconn.setRequestProperty("accept", "application/json");


            harClseconn.setRequestProperty("Authorization", authMap.get(user));

            OutputStream hardCloseOSs = harClseconn.getOutputStream();

            hardCloseOSs.flush();

            System.out.println(harClseconn.getResponseMessage());
        }
    }







}
