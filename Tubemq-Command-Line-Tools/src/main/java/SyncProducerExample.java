import org.apache.commons.codec.binary.StringUtils;
import org.apache.tubemq.client.config.TubeClientConfig;
import org.apache.tubemq.client.factory.MessageSessionFactory;
import org.apache.tubemq.client.factory.TubeSingleSessionFactory;
import org.apache.tubemq.client.producer.MessageProducer;
import org.apache.tubemq.client.producer.MessageSentResult;
import org.apache.tubemq.corebase.Message;

public final class SyncProducerExample {

    public static void main(String[] args) throws Throwable {
        final String masterHostAndPort = "localhost:8715";
        final TubeClientConfig clientConfig = new TubeClientConfig(masterHostAndPort);
        final MessageSessionFactory messageSessionFactory = new TubeSingleSessionFactory(clientConfig);
        final MessageProducer messageProducer = messageSessionFactory.createProducer();
        final String topic = "testTopic";
        final String body = "This is a test message from single-session-factory!";
        byte[] bodyData = StringUtils.getBytesUtf8(body);
        messageProducer.publish(topic);
        Message message = new Message(topic, bodyData);
        MessageSentResult result = messageProducer.sendMessage(message);
        if (result.isSuccess()) {
            System.out.println("sync send message : " + message);
        }
        messageProducer.shutdown();
    }
}