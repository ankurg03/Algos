package kafkaclient;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import java.util.stream.Stream;

public class KafkaProducerExample {

    private final static String TOPIC = "cdm_traffic.testPayloads2";
    private final static String BOOTSTRAP_SERVERS =
            "10.34.29.76:9092,10.33.18.178:9092,10.33.51.147:9092";
    private final static String FILE_PATH = "/tmp/paylods";
    static int validRecord =0 ;
    static int totalRecord =0;
    static HashMap<String ,Integer> hashMap = new HashMap<>();

    public static Producer<Long, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "adhoc");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    public static void read() throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(FILE_PATH))) {
            stream.filter(KafkaProducerExample::canInsert).
                    map(KafkaProducerExample::updatePayload).forEach(s -> {validRecord++;});
        }
    }


    public static void produce() throws IOException {
        Producer<Long, String> producer = createProducer();
        List<Future<RecordMetadata>> waitingForAck = new ArrayList<>();
        final long[] offset = {0};
        final int[] partition = {0};
        try (Stream<String> stream = Files.lines(Paths.get(FILE_PATH))) {
            stream.filter(KafkaProducerExample::canInsert).
                    map(KafkaProducerExample::updatePayload).forEach(payload -> {
                waitingForAck.add(producer.send(new ProducerRecord<>(TOPIC, payload)));
                if (waitingForAck.size() > 30) {
                    waitingForAck.stream().forEach(future -> {
                        try {
                            RecordMetadata get = future.get();
                            offset[0] = get.offset();
                            partition[0] = get.partition();

                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    });
                    waitingForAck.clear();
                    System.out.println(""+partition[0] + "-" + offset[0]);
                }
            });
            waitingForAck.stream().forEach(future -> {
                try {
                    RecordMetadata get = future.get();
                    offset[0] = get.offset();
                    partition[0] = get.partition();

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
            waitingForAck.clear();
            System.out.println(""+partition[0] + "-" + offset[0]);
        }

    }

    private static String updatePayload(String payload) {
        return payload;
    }

    private static boolean canInsert(String payload) {
        totalRecord ++;
        String ts = (((payload.split("\"timestamp\":"))[1]).split(","))[0];
        Date date = new Date(Long.parseLong(ts));
        String s = date.getDate() + "-"+ date.getMonth();
        if(new Random().nextInt(5000) ==300)
            System.out.println("processed = " + totalRecord);
       // System.out.println(s);
        if(s.equals("8-4"))
            return true;
        return false;
    }

    public static void main(String[] args) throws IOException {

        produce();



    }
}
