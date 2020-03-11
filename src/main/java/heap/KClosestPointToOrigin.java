package heap;

import java.util.PriorityQueue;

public class KClosestPointToOrigin {

//    https://leetcode.com/problems/k-closest-points-to-origin/submissions/
    public static void main(String[] args) {

//        int[][] input = {{1,3},{-2,2}};
        int[][] input = {{3,3},{5,-1},{-2,4}};
        int[][] ans = kClosest(input, 2);
        System.out.println(ans[0][0]+","+ans[0][1]);
    }

    public static int[][] kClosest(int[][] points, int K) {
        int[][] ans = new int[K][2];
        if (K ==0 ) return ans;
        PriorityQueue<int[]> maxHeap = new PriorityQueue<int[]>((point1, point2) ->
            (point2[0] * point2[0] + point2[1] * point2[1]) - (point1[0] * point1[0] + point1[1] * point1[1])
        );
        for(int[] point : points) {
            maxHeap.add(point);
            if (maxHeap.size() > K) maxHeap.poll();
        }
        int i = 0;
        while (!maxHeap.isEmpty()) {
            System.out.println(maxHeap.peek()[0] +","+maxHeap.peek()[1]);
            ans[i] = maxHeap.poll();
            i++;
        }
        return ans;
    }
}
