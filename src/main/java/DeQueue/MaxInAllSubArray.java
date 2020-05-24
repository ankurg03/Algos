package DeQueue;

import java.util.ArrayDeque;
import java.util.Deque;

//#Amazon
// Find max element in all sub array of size K
public class MaxInAllSubArray {
    static int array[] = {4, 9 , 6, 5, 8, 10, 12};
//    static int array[] = {4, 9 };
    static int k = 3;
    //ans 9, 9, 8, 10, 12


    public static void main(String[] args) {
        findMax(array, k);
    }

    private static void findMax(int[] array, int k) {
        Deque<Integer> deque = new ArrayDeque<>();
        int windowStart = 0;
        int windowEnd = k-1;
        if(array.length - 1 < windowEnd) {
            updateDeque(deque, windowStart, array.length -1, array);
        }
        for(int i =0; i < array.length - k + 1; i++ ){
            updateDeque(deque, windowStart, windowEnd, array);
            windowStart++;
            windowEnd++;
            while (deque.getFirst() < windowStart) {
                deque.removeFirst();
            }
        }

    }

    private static void updateDeque(Deque<Integer> deque, int windowStart, int windowEnd, int[] array) {
        if (windowEnd >= windowStart) {
            if (deque.isEmpty() || array[deque.getLast()] > array[windowStart]) {
                deque.add(windowStart);
                windowStart++;
            }
            else
                deque.removeLast();
            updateDeque(deque, windowStart, windowEnd, array);
        }
        else
            System.out.println(array[deque.getFirst()]);

    }




}
