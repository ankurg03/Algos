package heap;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class PriorityQueueExamples {
    public static void main(String[] args) {

        Integer[] arr = {5, 8, 4, 7, 6};
        List<Integer> integers = Arrays.asList(arr);
        createMaxHeap(integers);
        createMinHeap(integers);

    }

    private static void createMaxHeap(List<Integer> integers) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<Integer>(1, (x, y) -> y-x);
        priorityQueue.add(5);
        priorityQueue.add(8);
        priorityQueue.add(4);
        priorityQueue.add(7);
        priorityQueue.add(6);
        System.out.println(priorityQueue);
        System.out.println("peek" + priorityQueue.peek());
        System.out.println(priorityQueue);
        System.out.println("poll" + priorityQueue.poll());
        System.out.println(priorityQueue);
        System.out.println("poll" + priorityQueue.poll());
        System.out.println(priorityQueue);
        System.out.println("remove" + priorityQueue.remove());
        System.out.println(priorityQueue);
    }

    private static void createMinHeap(List<Integer> integers) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<Integer>(1, (x, y) -> x-y);
        priorityQueue.add(5);
        priorityQueue.add(8);
        priorityQueue.add(4);
        priorityQueue.add(7);
        priorityQueue.add(6);
        System.out.println(priorityQueue);
    }
}
