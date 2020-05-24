package problem.solving;

import java.util.List;

public class MedianFinder {

    /** initialize your data structure here. */

    int med1 = 0;
    int med2 = 0;
    boolean isMoveMed1 = true;
    boolean isMoveMed2 = true;


    public MedianFinder() {

    }

    public void addNum(int num) {

        if (isMoveMed1 && isMoveMed2) {
            med1 = num;
            med2 = num;
            isMoveMed1 = false;
            isMoveMed2 = true;
        }
        else if (isMoveMed1) {
            med1 = med2;
            isMoveMed2 = true;
            isMoveMed1 = false;
        }
        else if (isMoveMed2) {
            med2 = num;
            isMoveMed2 = false;
            isMoveMed1 = true;
        }

    }

    public double findMedian() {
        return 0.0;
    }

    public static void main(String[] args) {

    }
}
