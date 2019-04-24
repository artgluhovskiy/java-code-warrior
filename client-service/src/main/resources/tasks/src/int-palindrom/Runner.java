public class Runner {

    private Solution solution;

    public void run() {
        System.out.println("Runner is running!");
        test0();
        test1();
    }

    public void test0() {
        boolean result = solution.isPalindrome(121);
        if (!result) {
            throw new RuntimeException();
        }
    }

    public void test1() {
        boolean result = solution.isPalindrome(-121);
        if (result) {
            throw new RuntimeException();
        }
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }
}