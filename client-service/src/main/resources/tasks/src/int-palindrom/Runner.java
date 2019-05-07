import org.art.web.warrior.commons.driver.exception.ClientCodeExecutionException;

public class Runner {

    private Solution solution;

    public void run() {
        System.out.println("Runner is running!");
        test0();
        test1();
    }

    public void test0() {
        int input = 121;
        boolean excpected = true;
        boolean result = solution.isPalindrome(input);
        if (excpected != result) {
            throw new ClientCodeExecutionException("test0", excpected, result, input);
        }
    }

    public void test1() {
        int input = -121;
        boolean expected = false;
        boolean result = solution.isPalindrome(input);
        if (expected != result) {
            throw new ClientCodeExecutionException("test1", expected, result, input);
        }
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }
}