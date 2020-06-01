package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

public class SampleProducer {
    //initializing protocol buffer schema class
    TransactionOuterClass.Transaction.Builder transaction = TransactionOuterClass.Transaction.newBuilder();

    // generates random transaction data for 20 account numbers
    public TransactionOuterClass.Transaction gen_data() {
        transaction.clear();
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSSSSS");
        Date dateobj = new Date();
        double randomAmount = (Math.random() * ((10000.00 - 100.00) + 1)) + 100.00;
        String[] acc_numbers = {"4090000872969", "4090000358934", "4090009580282", "4090007525867", "4090003318142", "4090007380439", "4090002998810", "4090000608654", "4090001987091", "4090001797429", "4090000383991", "4090001752691", "4090009216541", "4090009991273", "4090001261321", "4090008077650", "4090007233629", "4090005947552", "4090004283593", "4090005908962"};
        int index=new Random().nextInt(acc_numbers.length);
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setAccountNumber(acc_numbers[index]);
        transaction.setTransactionReference(UUID.randomUUID().toString());
        transaction.setTransactionDatetime(df.format(dateobj));
        transaction.setAmount(Math.round(randomAmount * 100.00) / 100.00);
        return transaction.build();

    }

    public SampleProducer() throws InterruptedException {
        Properties properties = new Properties();

        //connect to multiple kafka brokers
        properties.put("bootstrap.servers", "167.71.235.115:9092,167.71.235.115:9093");

        //setting default serializer
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        //Creating topic in kafka
        String topic = "trans";
        String key = "transkey";

        //Creating Kafka Producer and generating 1000 transactions
        KafkaProducer kafkaProducer = new KafkaProducer<>(properties);
        for (int i = 1; i <= 1000; i++) {
            TransactionOuterClass.Transaction transactionData = gen_data();

            ProducerRecord producerRecord = new ProducerRecord(topic, key, transactionData.toByteArray());
            kafkaProducer.send(producerRecord);
            System.out.println("Count--"+i);
            System.out.println("data--"+transactionData);
            Thread.sleep(300);
        }

        kafkaProducer.close();


    }

    public static void main(String[] args) throws InterruptedException {
        SampleProducer sp = new SampleProducer();
    }
}
