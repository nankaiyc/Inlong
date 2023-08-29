import org.apache.tubemq.client.config.ConsumerConfig;
import org.apache.tubemq.client.consumer.ConsumePosition;

import org.apache.tubemq.client.consumer.MessageListener;
import org.apache.tubemq.client.consumer.PushMessageConsumer;
import org.apache.tubemq.client.factory.MessageSessionFactory;
import org.apache.tubemq.client.factory.TubeSingleSessionFactory;
import org.apache.tubemq.corebase.Message;


import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class PushConsumer {

    public static void main(String[] args) throws Throwable {
        ReceiveMessage("testTopic","localhost:8715","test-group1");
    }

    public static void ReceiveMessage(String topicName, String master, String consumergroup) throws Throwable{
        final String masterHostAndPort = master;
        final String topic = topicName;
        final String group = consumergroup;
        final ConsumerConfig consumerConfig = new ConsumerConfig(masterHostAndPort, group);
        consumerConfig.setConsumePosition(ConsumePosition.CONSUMER_FROM_LATEST_OFFSET);
        final MessageSessionFactory messageSessionFactory = new TubeSingleSessionFactory(consumerConfig);
        final PushMessageConsumer pushConsumer = messageSessionFactory.createPushConsumer(consumerConfig);
        ((PushMessageConsumer) pushConsumer).subscribe(topic, null, new MessageListener() {
            @Override
            public void receiveMessages(List<Message> messages) throws InterruptedException {
//                System.out.println("Open Consumption Channel Successfully.");
                for (Message message : messages) {
                    System.out.println("received message : " + new String(message.getData()));
                }
            }

            @Override
            public Executor getExecutor() {
                return null;
            }
            @Override
            public void stop() {
                //
            }
        });
        pushConsumer.completeSubscribe();
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(10, TimeUnit.MINUTES);
    }
}