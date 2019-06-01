import org.art.web.warrior.commons.driver.exception.ClientCodeExecutionException;

public class Runner {

    private Solution solution;

    public void run() {
        System.out.println("Running the 'Int Palindrome' coding problem solution!");
        test0();
        test1();
    }

    public void test0() {
        int input = 121;
        boolean expected = true;
        boolean result = solution.isPalindrome(input);
        if (expected != result) {
            throw new ClientCodeExecutionException("test0", String.valueOf(expected), String.valueOf(result), String.valueOf(input));
        }
    }

    public void test1() {
        int input = -121;
        boolean expected = false;
        boolean result = solution.isPalindrome(input);
        if (expected != result) {
            throw new ClientCodeExecutionException("test1", String.valueOf(expected), String.valueOf(result), String.valueOf(input));
        }
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }
}