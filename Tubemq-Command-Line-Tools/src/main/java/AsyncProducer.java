import org.apache.commons.codec.binary.StringUtils;
import org.apache.tubemq.client.config.TubeClientConfig;
import org.apache.tubemq.client.factory.MessageSessionFactory;
import org.apache.tubemq.client.factory.TubeSingleSessionFactory;
import org.apache.tubemq.client.producer.MessageProducer;
import org.apache.tubemq.client.producer.MessageSentCallback;
import org.apache.tubemq.client.producer.MessageSentResult;
import org.apache.tubemq.corebase.Message;

import java.util.Map;

public final class AsyncProducer {

    public static void main(String[] args) throws Throwable {
        SendMessage("testTopic","async send message from single-session-factory!","localhost:8715");
    }

    public static void SendMessage(String topicName, String messageContent, String master) throws Throwable{
        final String masterHostAndPort = master;
        final TubeClientConfig clientConfig = new TubeClientConfig(masterHostAndPort);
        final MessageSessionFactory messageSessionFactory = new TubeSingleSessionFactory(clientConfig);
        final MessageProducer messageProducer = messageSessionFactory.createProducer();
        final String topic = topicName;
        final String body = messageContent;
        byte[] bodyData = StringUtils.getBytesUtf8(body);
        messageProducer.publish(topic);
        final Message message = new Message(topic, bodyData);
        messageProducer.sendMessage(message, new MessageSentCallback() {
            @Override
            public void onMessageSent(MessageSentResult result) {
                if (result.isSuccess()) {
                    System.out.println("async send message successfully");
                } else {
                    System.out.println("async send message failed : " + result.getErrMsg());
                }
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("async send message error : " + e);
            }
        });
        messageProducer.shutdown();
    }
}