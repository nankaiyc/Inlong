import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TubeMQTopicCreator {

    private static final String API_URL = "http://127.0.0.1:8080/webapi.htm?type=op_modify&method=";
    public static void main(String[] args) {
        // Test with default parameters
//        TubeMQTopicCreator.createTopic("testTopic4", null);

        // Test with custom parameters
        Map<String, String> customParameters = new HashMap<>();
//        customParameters.put("deleteWhen", "0 0 6,18 * * ?");
//        customParameters.put("deletePolicy", "delete,168");
        customParameters.put("numPartitions", "5");
//        customParameters.put("asdasdas", "fasdfasdf");
//         ... add other custom parameters ...

        createTopic("customTopic", customParameters);
    }
    public static void createTopic(String topicName, Map<String, String> addTopicParameters) {
        String brokerId = "1";
        String createUser = "qyc";
        String confModAuthToken = "abc";
        String method = "admin_add_new_topic_record";

        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(API_URL + method);

            StringBuilder data = new StringBuilder("topicName=" + topicName +
                    "&brokerId=" + brokerId +
                    "&createUser=" + createUser +
                    "&confModAuthToken=" + confModAuthToken);

            if (addTopicParameters != null) {
                for (Map.Entry<String, String> entry : addTopicParameters.entrySet()) {
                    data.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }

            StringEntity entity = new StringEntity(data.toString());

            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
//            System.out.println(data);
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
                        System.out.println("Create topic " + topicName + " successfully.");
                    } else {
                        String errMsg = errdata.getJSONObject(0).getString("errInfo");
                        System.err.println("Error creating topic: " + errMsg);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error creating topic: " + e.getMessage());
        }
    }
}
