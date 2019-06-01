import java.util.LinkedList;

public class Solution {

    public boolean isValid(String s) {
        LinkedList<Character> stack = new LinkedList<>();
        for (char ch : s.toCharArray()) {
            if (ch == '(' || ch == '[' || ch == '{') {
                stack.push(ch);
            } else {
                if (!stack.isEmpty() && isPaired(stack.peek(), ch)) {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    private boolean isPaired(char ch1, char ch2) {
        return ch1 == '(' && ch2 == ')' || ch1 == '{' && ch2 == '}' || ch1 == '[' && ch2 == ']';
    }
}