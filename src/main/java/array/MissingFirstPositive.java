package array;

public class MissingFirstPositive {

    static int VISITED = -9999;
    public static void main(String[] args) {
//        int [] arr = {3,4,-1,1};
//        int [] arr = {1,2,2,2,2,3,3,3,3};
        int [] arr = {-4, 9, 7, 11, -3, -8, 1, 2, 3, 5, 6};
    //    int [] arr = {2,1};
        System.out.println(findMissingPositive(arr));
    }


    public static int findMissingPositive(int nums[]) {
        int index =0 ;
        while(index < nums.length) {

            if (nums[index] -1 >=0 && nums[index]- 1 < nums.length) {
                if (nums[index]-1 == index) {
                    nums[index] = VISITED;
                    index++;
                }
                else {
                    int visitedIndex = nums[index] -1;
                    if(nums[nums[index] -1] != VISITED) nums[index] = nums[nums[index] -1];
                    else index++;
                    nums[visitedIndex] = VISITED;
                }
            }
            else {
                index++;
            }
        }
        for(int n =0 ; n< nums.length; n++) {
            if (nums[n] != VISITED) return n+1;
        }
        return nums.length+1;
    }
}
