import org.art.web.warrior.commons.driver.exception.ClientCodeExecutionException;

public class Runner {

    private Solution solution;

    public void run() {
        System.out.println("Running the 'Valid Parentheses' coding problem solution!");
        test0();
        test1();
        test2();
        test3();
        test4();
        test5();
    }

    public void test0() {
        String input = "()";
        boolean expected = true;
        boolean result = solution.isValid(input);
        if (expected != result) {
            throw new ClientCodeExecutionException("test0", String.valueOf(expected), String.valueOf(result), String.valueOf(input));
        }
    }

    public void test1() {
        String input = "{()}";
        boolean expected = true;
        boolean result = solution.isValid(input);
        if (expected != result) {
            throw new ClientCodeExecutionException("test1", String.valueOf(expected), String.valueOf(result), String.valueOf(input));
        }
    }

    public void test2() {
        String input = "{([])}";
        boolean expected = true;
        boolean result = solution.isValid(input);
        if (expected != result) {
            throw new ClientCodeExecutionException("test2", String.valueOf(expected), String.valueOf(result), String.valueOf(input));
        }
    }

    public void test3() {
        String input = "{}[]()";
        boolean expected = true;
        boolean result = solution.isValid(input);
        if (expected != result) {
            throw new ClientCodeExecutionException("test3", String.valueOf(expected), String.valueOf(result), String.valueOf(input));
        }
    }

    public void test4() {
        String input = "{}[()]()";
        boolean expected = true;
        boolean result = solution.isValid(input);
        if (expected != result) {
            throw new ClientCodeExecutionException("test4", String.valueOf(expected), String.valueOf(result), String.valueOf(input));
        }
    }

    public void test5() {
        String input = "{}[(()])()";
        boolean expected = false;
        boolean result = solution.isValid(input);
        if (expected != result) {
            throw new ClientCodeExecutionException("test5", String.valueOf(expected), String.valueOf(result), String.valueOf(input));
        }
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }
}