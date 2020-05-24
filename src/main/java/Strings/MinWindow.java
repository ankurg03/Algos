package Strings;

import java.util.HashMap;
import java.util.Map;

public class MinWindow {

    public static void main(String[] args) {
        String stream = "aa";
        String window = "aa";

        System.out.println(minWindow(stream, window));


    }

    public static String minWindow(String s, String t) {
        Map<Character,Integer> map = new HashMap();
        int foundChars = 0;
        int ansStartPtr = 0;
        int startPtr = 0;
        int endPtr = 0;
        int diff = s.length() +1; //tobe safer use long here
        boolean foundOnce = false;

        for (char c : t.toCharArray()) {
            map.put(c, map.getOrDefault(c,0) +1);
        }


        for (int i =0; i< s.length(); i++) {

            if (map.containsKey(s.charAt(i))) {
                int newValue = map.get(s.charAt(i))-1;
                map.put(s.charAt(i), newValue);

                if(newValue >=0) foundChars++;

                if (foundChars == t.length()) {
                    foundOnce = true;
                    //shrink window
                    while (startPtr < s.length()) {
                        if(map.containsKey(s.charAt(startPtr))) {
                            int value = map.get(s.charAt(startPtr));
                            if (value == 0) {
                                break;
                            }
                            map.put(s.charAt(startPtr), value + 1); //removing an element
                        }
                        startPtr++;
                    }

                    int cdiff = i -startPtr;
                    if (cdiff < diff) {
                        endPtr = i; //update answer
                        ansStartPtr = startPtr;
                        diff = cdiff;
                    }

                    if(map.containsKey(s.charAt(startPtr))) {
                        map.put(s.charAt(startPtr), map.get(s.charAt(startPtr)) + 1);
                        startPtr++;
                        foundChars--;
                    }
                }
            }
        }
        if(foundOnce) return s.substring(ansStartPtr, endPtr+1);
        return "";
    }


}
