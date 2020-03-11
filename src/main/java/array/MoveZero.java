package array;

public class MoveZero {
    public static void main(String[] args) {
//        int [] nums = {1,2,0,0,3};
        int [] nums = {1,2};
        moveZeroes(nums);
        for(int i:nums) {
            System.out.print(" "+i);
        }
                    //1,2,0,0,3
    }


    public static void moveZeroes(int[] nums) {
        if (nums.length <=1 ) return;

        int i=0;
        int j=0;
        while (j< nums.length && i< nums.length) {
            while ( i< nums.length && nums[i] !=0 ) i++;
            j = i+1;

            while ( j< nums.length && nums[j] ==0 ) j++;

            if (j >= nums.length) continue;
            nums[i] = nums[j];
            nums[j] = 0;
            i++;
            j++;
        }

    }
}
