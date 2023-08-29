import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TubeMQTopicDelete {

    private static final String API_URL = "http://127.0.0.1:8080/webapi.htm?type=op_modify&method=";

    public static void main(String[] args) {
        // Example call to delete topic
        softdeleteTopic("demo3");
        hardDeleteTopic("demo3");

    }
    public static void softdeleteTopic(String topicName) {
        String brokerId = "1";
        String modifyUser = "qyc";
        String confModAuthToken = "abc";
        String method = "admin_delete_topic_info";

        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(API_URL + method);

            String data = "topicName=" + topicName +
                    "&brokerId=" + brokerId +
                    "&modifyUser=" + modifyUser +
                    "&confModAuthToken=" + confModAuthToken;

            StringEntity entity = new StringEntity(data);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(responseEntity.getContent(), StandardCharsets.UTF_8))) {
                    String line;
                    StringBuilder responseStr = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        responseStr.append(line);
                    }

                    JSONObject jsonResponse = new JSONObject(responseStr.toString());
//                    System.out.println(jsonResponse);
                    JSONArray errdata = jsonResponse.getJSONArray("data");
                    int errCode = errdata.getJSONObject(0).getInt("errCode");
//                    System.out.println(errCode);
                    if (errCode == 200) {
                        System.out.println("Delete topic " + topicName + " successfully.");
                    } else {
                        String errMsg = errdata.getJSONObject(0).getString("errInfo");
                        System.err.println("Error deleting topic: " + errMsg);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error deleting topic: " + e.getMessage());
        }
    }

    public static void hardDeleteTopic(String topicName) {
        String brokerId = "1";
        String modifyUser = "qyc";
        String confModAuthToken = "abc";
        String method = "admin_remove_topic_info";

        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(API_URL + method);

            String data = "topicName=" + topicName +
                    "&brokerId=" + brokerId +
                    "&modifyUser=" + modifyUser +
                    "&confModAuthToken=" + confModAuthToken;

            StringEntity entity = new StringEntity(data);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(responseEntity.getContent(), StandardCharsets.UTF_8))) {
                    String line;
                    StringBuilder responseStr = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        responseStr.append(line);
                    }

                    JSONObject jsonResponse = new JSONObject(responseStr.toString());
//                    System.out.println(jsonResponse);
                    JSONArray errdata = jsonResponse.getJSONArray("data");
                    int errCode = errdata.getJSONObject(0).getInt("errCode");
//                    System.out.println(errCode);

                    if (errCode == 200) {
                        System.out.println("Hard delete topic " + topicName + " successfully.");
                    } else {
                        String errMsg = errdata.getJSONObject(0).getString("errInfo");
                        System.err.println("Error hard deleting topic: " + errMsg);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error hard deleting topic: " + e.getMessage());
        }
    }
}
