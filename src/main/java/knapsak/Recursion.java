package knapsak;

public class Recursion {

   static int minCost = -1;
    public static void main(String[] args) {
        int [] w = {2, 3, 3, 4};
        int [] p = {1, 2, 5, 9};
        int capacity = 7;
        int ptr =0;
        getCost(w, p, capacity, ptr, 0);
        System.out.println(minCost);
    }

    private static void getCost(int[] w, int[] p, int capacity, int ptr, int cost) {
        if(ptr < w.length) {
            getCost(w, p, capacity, ptr+1, cost);
            if (capacity >= w[ptr]) {
                capacity -= w[ptr];
                cost += p[ptr];
                if (capacity == 0 ) {
                    if (minCost == -1) minCost = cost;
                    else if (minCost > cost) minCost = cost;
                }
                else getCost(w, p, capacity, ptr+1, cost);
            }
        }
    }
}



/*
DP
               1 2 3 4 5 6 7
           0 0 0 0 0 0 0 0
           1 0   1
           2 0
           3 0
           4 0


 */