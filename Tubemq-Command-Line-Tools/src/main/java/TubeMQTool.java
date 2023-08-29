import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.*;

public class TubeMQTool {


    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("h", "help", false, "Show help");
        options.addOption("t", "topic", true, "Topic name");
        options.addOption("c", "create", false, "Create topic");
        options.addOption("d", "delete", false, "Delete topic");
        options.addOption("l", "list", false, "List topics");
        options.addOption("u", "update", false, "Update topic");
        options.addOption("s", "send", true, "Send message");
        options.addOption("m", "consume", false, "Consume message");
        options.addOption("master", true, "Master IP");
        options.addOption("consumergroup",  true, "Consumergroup");
        options.addOption("deleteWhen", true, "Delete when settings");
        options.addOption("deletePolicy", true, "Delete policy settings");
        options.addOption("numPartitions", true, "Number of partitions");
        options.addOption("unflushThreshold", true, "Unflush threshold settings");
        options.addOption("unflushInterval", true, "Unflush interval settings");
        options.addOption("unflushDataHold", true, "Unflush data hold settings");
        options.addOption("numTopicStores", true, "Number of topic stores");
        options.addOption("memCacheMsgCntInK", true, "Memory cache message count in K");
        options.addOption("memCacheMsgSizeInMB", true, "Memory cache message size in MB");
        options.addOption("memCacheFlushIntvl", true, "Memory cache flush interval");
        options.addOption("acceptPublish", true, "Accept publish setting");
        options.addOption("acceptSubscribe", true, "Accept subscribe setting");
        options.addOption("maxMsgSizeInMB", true, "Maximum message size in MB");
        options.addOption("createDate", true, "Create date");
        options.addOption("modifyDate", true, "Modify date");
        // ...

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                printHelp(options);
            } else if (cmd.hasOption("l")) {
                if (cmd.hasOption("t")) {
                    String topic = cmd.getOptionValue("t");
                    listTopics(topic);
                }
                else{
                    listTopics(null);
                }
            } else if (cmd.hasOption("t")) {
                String topic = cmd.getOptionValue("t");
                if (cmd.hasOption("c")) {
                    createTopic(topic, cmd);
                } else if (cmd.hasOption("d")) {
                    deleteTopic(topic);
                } else if (cmd.hasOption("s")) {
                    String message = cmd.getOptionValue("s");
                    if(cmd.hasOption("master")){
                        String master = cmd.getOptionValue("master");
                        sendMessage(topic, message, master);
                    }
                    else{
                        System.out.println("Please provide a master.");
                    }
                } else if (cmd.hasOption("m")) {
                    if(cmd.hasOption("master")){
                        String master = cmd.getOptionValue("master");
                        if(cmd.hasOption("consumergroup")){
                            String consumergroup = cmd.getOptionValue("consumergroup");
                            consumeMessage(topic,master,consumergroup);
                        }
                        else{
                            System.out.println("Please provide a consumergroup.");
                        }
                    }
                    else{
                        System.out.println("Please provide a master.");
                    }

                } else if (cmd.hasOption("u")) {
                    updateTopic(topic, cmd);
                } else {
                    System.out.println("Invalid arguments. Please provide valid options.");
                }
            } else {
                System.out.println("Please provide a topic name.");
            }
        } catch (ParseException e) {
            System.err.println("Error parsing command line arguments: " + e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("TubeMQTool", options);
    }

    private static void createTopic(String topic, CommandLine cmd) {
//        System.out.println("Creating topic: " + topic);
//        System.out.println("params: " + getAddTopicParameters(cmd));
        TubeMQTopicCreator.createTopic(topic, getTopicParameters(cmd));
    }
    private static Map<String, String> getTopicParameters(CommandLine cmd) {
        Map<String, String> parameters = new HashMap<>();

        // Delete When
        if (cmd.hasOption("deleteWhen")) {
            parameters.put("deleteWhen", cmd.getOptionValue("deleteWhen"));
        }
        // Delete Policy
        if (cmd.hasOption("deletePolicy")) {
            parameters.put("deletePolicy", cmd.getOptionValue("deletePolicy"));
        }
        // Number of Partitions
        if (cmd.hasOption("numPartitions")) {
            parameters.put("numPartitions", cmd.getOptionValue("numPartitions"));
        }
        // Unflush Threshold
        if (cmd.hasOption("unflushThreshold")) {
            parameters.put("unflushThreshold", cmd.getOptionValue("unflushThreshold"));
        }
        // Unflush Interval
        if (cmd.hasOption("unflushInterval")) {
            parameters.put("unflushInterval", cmd.getOptionValue("unflushInterval"));
        }
        // Unflush Data Hold
        if (cmd.hasOption("unflushDataHold")) {
            parameters.put("unflushDataHold", cmd.getOptionValue("unflushDataHold"));
        }
        // Number of Topic Stores
        if (cmd.hasOption("numTopicStores")) {
            parameters.put("numTopicStores", cmd.getOptionValue("numTopicStores"));
        }
        // Mem Cache Msg Count in K
        if (cmd.hasOption("memCacheMsgCntInK")) {
            parameters.put("memCacheMsgCntInK", cmd.getOptionValue("memCacheMsgCntInK"));
        }
        // Mem Cache Msg Size in MB
        if (cmd.hasOption("memCacheMsgSizeInMB")) {
            parameters.put("memCacheMsgSizeInMB", cmd.getOptionValue("memCacheMsgSizeInMB"));
        }
        // MemCache Flush Interval
        if (cmd.hasOption("memCacheFlushIntvl")) {
            parameters.put("memCacheFlushIntvl", cmd.getOptionValue("memCacheFlushIntvl"));
        }
        // Accept Publish
        if (cmd.hasOption("acceptPublish")) {
            parameters.put("acceptPublish", cmd.getOptionValue("acceptPublish"));
        }
        // Accept Subscribe
        if (cmd.hasOption("acceptSubscribe")) {
            parameters.put("acceptSubscribe", cmd.getOptionValue("acceptSubscribe"));
        }
        // Max Msg Size in MB
        if (cmd.hasOption("maxMsgSizeInMB")) {
            parameters.put("maxMsgSizeInMB", cmd.getOptionValue("maxMsgSizeInMB"));
        }
        // Create Date
        if (cmd.hasOption("createDate")) {
            parameters.put("createDate", cmd.getOptionValue("createDate"));
        }
        // Modify Date (for update)
        if (cmd.hasOption("modifyDate")) {
            parameters.put("modifyDate", cmd.getOptionValue("modifyDate"));
        }

        return parameters;
    }

    private static void deleteTopic(String topic) {
        System.out.println("Deleting topic: " + topic);
        // Implement the logic to delete a TubeMQ topic here
        TubeMQTopicDelete.softdeleteTopic(topic);
        TubeMQTopicDelete.hardDeleteTopic(topic);
    }

    private static void listTopics(String topic) {
//        System.out.println("Listing topic: " + topic);
        TubeMQTopicQuery.queryTopicInfo(topic);
    }

    private static void sendMessage(String topic, String message, String master) throws Throwable {
//        System.out.println("Sending message to topic: " + topic);
//        System.out.println("Message: " + message);
//        System.out.println("Master: " + master);
        // Implement the logic to send a message to a TubeMQ topic here
        AsyncProducer.SendMessage(topic,message,master);
    }

    private static void consumeMessage(String topic, String master, String consumergroup) throws Throwable{
//        System.out.println("Consuming messages from topic: " + topic);
//        System.out.println("Master: " + master);
//        System.out.println("Consumergroup: " + consumergroup);
        // Implement the logic to consume messages from a TubeMQ topic here
        PushConsumer.ReceiveMessage(topic,master,consumergroup);

    }

    private static void updateTopic(String topic, CommandLine cmd) {
//        String acceptPublish = cmd.getOptionValue("acceptPublish");
//        System.out.println("Updating topic: " + topic);
        // Implement the logic to update a TubeMQ topic here
        TubeMQTopicModify.modifyTopicInfo(topic, getTopicParameters(cmd));
    }
}



