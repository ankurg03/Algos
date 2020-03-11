package dp.clibm_stair;

/**
 * https://leetcode.com/problems/climbing-stairs/
 */
public class ClimbStair {

    public static void main(String[] args) {
        System.out.println(climbStairs(5));
    }

    public static int climbStairs(int n) {
        if (n <= 1)
            return 1;
        int stepsAt1 = 1;
        int stepsAt2 = 2;

        for (int i = 3; i <= n; i++) {
            int tmp = stepsAt1 + stepsAt2;
            stepsAt1 = stepsAt2;
            stepsAt2 = tmp;

        }
        return stepsAt2;



    }


}
