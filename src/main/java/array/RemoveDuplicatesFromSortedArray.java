package array;

public class RemoveDuplicatesFromSortedArray {

    public static void main(String[] args) {
//        int [] nums = {1,1,1,3,3};

//        int [] nums = {0,0,1,1,1,2,2,3,3,4};
        int [] nums = {1,1,2};
        System.out.println(removeDuplicates(nums));

    }

    public static int removeDuplicates(int[] nums) {
        if (nums.length <= 1) {
            return nums.length;
        }

        int i = 0;
        int j=1;
        while(j<nums.length) {
            if(nums[i] == nums[j] && j < nums.length) j++;

            else {
                i++;
                if(i<j) nums[i]=nums[j];
            }

        }
        return i+1;


    }
}
