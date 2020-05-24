package Strings;

public class ValidAnagram {
    public static void main(String[] args) {
        String s = "anagram";
        String t = "nagaram";
        System.out.println(isValid(s, t));
    }
    public static boolean isValid(String s, String t) {
        int [] array = new int[256];
        for (char c : s.toCharArray()) {
            array[c] += 1;
        }
        for (char c : t.toCharArray()) {
            if (array[c] > 0) array[c] -= 1;
            else return false;
        }

        for(int i = 0; i< array.length; i++){
            if (array[i] != 0) return false;
        }
        return true;
    }
}
