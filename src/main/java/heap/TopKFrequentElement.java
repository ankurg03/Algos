package heap;

import java.util.*;


public class TopKFrequentElement {
    //https://leetcode.com/problems/top-k-frequent-elements/submissions/

    public static void main(String[] args) {
        int nums [] = {2,3,4,3,3,3,4,4,4,2,5,5,5,5,5,5,5,6,4};
        System.out.println(topKFrequent(nums,2));
    }

    public static List<Integer> topKFrequent(int[] nums, int k) {
        List<Integer> ans = new ArrayList<>();
        if (k == 0) return ans;
        Map<Integer,Integer> hashMap = new HashMap<Integer, Integer>();
        for(int n : nums) {
            hashMap.put(n, hashMap.getOrDefault(n, 0) + 1);
        }
        /* defining heap which is storing keys but comparing values of hash */
        PriorityQueue<Integer> heap = new PriorityQueue<>((k1, k2)-> hashMap.get(k1)-hashMap.get(k2));
        hashMap.keySet().forEach(ke -> {
            heap.add(ke);
            if (heap.size() > k) {
                heap.poll();
            }
        });
        while (!heap.isEmpty()){
            ans.add(heap.poll());
        }
        return ans;
    }
}
