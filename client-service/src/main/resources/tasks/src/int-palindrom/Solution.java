public class Solution {

    public boolean isPalindrome(int x) {
        int initialNum = x;
        int reversed = 0;
        if (x < 0) {
            return false;
        }
        while (x != 0) {
            reversed = reversed * 10 + x % 10;
            x = x / 10;
        }
        return initialNum == reversed;
    }
}