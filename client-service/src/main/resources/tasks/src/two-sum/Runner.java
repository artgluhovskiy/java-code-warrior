import org.art.web.warrior.commons.driver.exception.ClientCodeExecutionException;

import java.util.Arrays;

public class Runner {

    private Solution solution;

    public void run() {
        System.out.println("Running the 'Two Sum' coding problem solution!");
        test0();
        test1();
    }

    public void test0() {
        int[] inputArray = {2, 7, 11, 15};
        int targetVal = 9;
        Object[] input = new Object[] {inputArray, targetVal};
        int[] expected = {0, 1};
        int[] result = solution.twoSum(inputArray, targetVal);
        if (!Arrays.equals(expected, result)) {
            throw new ClientCodeExecutionException("test0", Arrays.toString(expected), Arrays.toString(result), Arrays.deepToString(input));
        }
    }

    public void test1() {
        int[] inputArray = {2, 7, 11, 15};
        int targetVal = 22;
        Object[] input = new Object[] {inputArray, targetVal};
        int[] expected = {1, 3};
        int[] result = solution.twoSum(inputArray, targetVal);
        if (!Arrays.equals(expected, result)) {
            throw new ClientCodeExecutionException("test1", Arrays.toString(expected), Arrays.toString(result), Arrays.deepToString(input));
        }
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }
}