package fileparser;


import com.yahoo.sketches.theta.PairwiseSetOperations;
import com.yahoo.sketches.theta.UpdateSketch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class PayloadGenerator {

    int executors = 12;
    int perExecutorInsertInSketch1 = 10000000; //10M
    int perExecutorInsertInSketch2 = 333333; //3.3L
    String FILE_PATH = "/grid/vdb/ankur/payload";

//    int perExecutorInsertInSketch1 = 100; //10M
//    int perExecutorInsertInSketch2 = 33; //3.3L

    HashMap<String, Long> counter;
    private static BufferedWriter buffWriter;

    public void init() {
        try {
            buffWriter = new BufferedWriter(new FileWriter(FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        counter = new HashMap<>();
    }

    private static void writeInFile(String record) {
        try {
            buffWriter.write(record + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Runnable generateAndWriteToFile = () -> {
        for (int i =0; i< perExecutorInsertInSketch1; i++) {
            synchronized (this) {
                counter.merge(Thread.currentThread().getName(), 1L, Long::sum);
                String random = UUID.randomUUID().toString();
                if (Math.round(Math.random() * 100000) == 100L)
                    System.out.println(Thread.currentThread().getName() + " remaining % " +
                            100 * (perExecutorInsertInSketch1 - counter.get(Thread.currentThread().getName())) / perExecutorInsertInSketch1);

                if (counter.get(Thread.currentThread().getName()) <= perExecutorInsertInSketch1)
                    writeInFile("{\"sketch\":\""+random+"\", \"dim\":\"dim1\", \"ts\":1590999725000}");

                if (counter.get(Thread.currentThread().getName()) <= perExecutorInsertInSketch2)
                    writeInFile("{\"sketch\":\""+random+"\", \"dim\":\"dim2\", \"ts\":1590999725000}");
                if (counter.get(Thread.currentThread().getName()) > perExecutorInsertInSketch1 &&
                        counter.get(Thread.currentThread().getName()) > perExecutorInsertInSketch2)
                    break;
            }
        }
    };

    public void testIntersection() throws ExecutionException, InterruptedException, TimeoutException, IOException {
        init();

        List<Thread> threads = new ArrayList<>();
        for(int i = 1; i<=executors; i ++) {
            Thread thread = new Thread(generateAndWriteToFile);
            threads.add(thread);
            thread.start();
        }
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        int randomAddInsketch2 = 1000000;
        insertInSketch2(randomAddInsketch2);
        close();

    }

    private void close() throws IOException {
        buffWriter.flush();
        buffWriter.close();
    }

    private void insertInSketch2(int num) {
        for (int i =0; i < num; i++)
            writeInFile("{\"sketch\":\""+UUID.randomUUID().toString()+"\", \"dim\":\"dim2\", \"ts\":1590999725000}");

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException, IOException {
        new PayloadGenerator().testIntersection();
    }
}
