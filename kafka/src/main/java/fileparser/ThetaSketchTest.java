package fileparser;

import com.yahoo.memory.Memory;

import com.yahoo.sketches.theta.*;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ThetaSketchTest {

    int executors = 12;
    int perExecutorInsertInSketch1 = 10000000; //10M
    int perExecutorInsertInSketch2 = 333333; //3.3L

//    int perExecutorInsertInSketch1 = 100; //10M
//    int perExecutorInsertInSketch2 = 33; //3.3L
    UpdateSketch sketch1;
    UpdateSketch sketch2;
    HashMap<String, Long> counter;

    public void initSketch() {
        sketch1 = Sketches.updateSketchBuilder().setNominalEntries(65536).build();
        sketch2 = Sketches.updateSketchBuilder().setNominalEntries(65536).build();
        counter = new HashMap<>();

    }


    Runnable insert = () -> {
        for (int i =0; i< perExecutorInsertInSketch1; i++) {
            synchronized (this) {
                counter.merge(Thread.currentThread().getName(), 1L, Long::sum);


                String random = UUID.randomUUID().toString();
                if (Math.round(Math.random() * 100000) == 100L)
                    System.out.println(Thread.currentThread().getName() + " remaining % " +
                            100 * (perExecutorInsertInSketch1 - counter.get(Thread.currentThread().getName())) / perExecutorInsertInSketch1);

                if (counter.get(Thread.currentThread().getName()) <= perExecutorInsertInSketch1)
                    sketch1.update(random);
                if (counter.get(Thread.currentThread().getName()) <= perExecutorInsertInSketch2)
                    sketch2.update(random);
                if (counter.get(Thread.currentThread().getName()) > perExecutorInsertInSketch1 &&
                        counter.get(Thread.currentThread().getName()) > perExecutorInsertInSketch2)
                    break;
            }
        }

    };

    public void testIntersection() throws ExecutionException, InterruptedException, TimeoutException {
        initSketch();
//        ExecutorService executor = Executors.newFixedThreadPool(executors);
        List<Thread> threads = new ArrayList<>();
        for(int i = 1; i<=executors; i ++) {
            Thread thread = new Thread(insert);
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

//        System.out.println(futuresList.size());
//        Thread.sleep(100000);
//        for (Future<Void> voidFuture : futuresList) {
//            if(voidFuture.isDone())
//                voidFuture.get(1, TimeUnit.HOURS);
//        }
//        executor.shutdown();
        System.out.println("Inserted in sketch1 "+ perExecutorInsertInSketch1 * executors);
        System.out.println("Sketch1 count " + Math.round(sketch1.getEstimate()));
        System.out.println("Sketch1 count error % "+ (((perExecutorInsertInSketch1 * executors) - sketch1.getEstimate())*100/(perExecutorInsertInSketch1 * executors)));
        long sketch2Count = (perExecutorInsertInSketch2 * executors)+randomAddInsketch2;
        System.out.println("Inserted in sketch2 "+ sketch2Count);
        System.out.println("Sketch2 count " + Math.round(sketch2.getEstimate()));
        System.out.println("Sketch2 error % "+ ((sketch2Count - sketch2.getEstimate())*100/sketch2Count));

        System.out.println("common record in sketches "+ (perExecutorInsertInSketch2 * executors));
        System.out.println("Intersection result " + Math.round(PairwiseSetOperations.intersect(sketch1.compact(), sketch2.compact()).getEstimate()));
        System.out.println("Intersection Error % " + 100*((perExecutorInsertInSketch2 * executors) -
                PairwiseSetOperations.intersect(sketch1.compact(), sketch2.compact()).getEstimate())/(perExecutorInsertInSketch2 * executors));
        System.out.println(PairwiseSetOperations.intersect(sketch1.compact(), sketch2.compact()));

    }

    private void insertInSketch2(int num) {
        for (int i =0; i < num; i++)
            sketch2.update(UUID.randomUUID().toString());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        new ThetaSketchTest().testIntersection();
    }
}
