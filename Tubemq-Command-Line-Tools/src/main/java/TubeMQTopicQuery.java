import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;


public class TubeMQTopicQuery {

    private static final String API_URL = "http://127.0.0.1:8080/webapi.htm?type=op_query&method=";

    public static void main(String[] args) {
        // 示例调用
        queryTopicInfo("demo3");
    }

    public static void queryTopicInfo(String topicName) {
        String method = "admin_query_topic_info";

        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(API_URL + method);

            StringBuilder dataBuilder = new StringBuilder();

            if (topicName != null) {
                dataBuilder.append("topicName=").append(topicName);
            }

            StringEntity entity = new StringEntity(dataBuilder.toString());
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
                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                    if (topicName == null) {
                        System.out.println("Following is the information for all topics:");
                    }

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject topicInfo = dataArray.getJSONObject(i);
                        JSONArray topicInfoArray = topicInfo.getJSONArray("topicInfo");

                        for (int j = 0; j < topicInfoArray.length(); j++) {
                            JSONObject topic = topicInfoArray.getJSONObject(j);
                            String currentTopicName = topic.getString("topicName");
                            System.out.println("Following is the information for topic " + currentTopicName + ":\n");
                            topic.keySet().forEach(key -> System.out.println(key + ": " + topic.get(key)));
                            System.out.println();
                        }
                    }

                    if (topicName != null && dataArray.length() == 0) {
                        System.out.println("Topic " + topicName + " does not exist.");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error querying topic info: " + e.getMessage());
        }
    }
}