package array;

import java.util.HashMap;
import java.util.HashSet;

public class AnyDuplicate {
    public static void main(String[] args) {

    }

    public static boolean anyDuplicate(int[] nums) {
        HashSet<Integer> hashSet = new HashSet<Integer>();

        for (int i : nums) {
            if (hashSet.contains(i))
                return false;
            hashSet.add(i);
        }
        return true;

    }
}
