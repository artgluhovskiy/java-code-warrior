package org.art.web.warrior.commons.driver.exception;

public class ClientCodeExecutionException extends RuntimeException {

    private String testName;

    private Object expected;

    private Object result;

    private Object input;

    public ClientCodeExecutionException(String testName, Object expected, Object result, Object input) {
        super();
        this.testName = testName;
        this.expected = expected;
        this.result = result;
        this.input = input;
    }

    @Override
    public String getMessage() {
        return "Test failed! Test name: " + testName + ". Expected: " + expected + ", but was: " + result + ". Input: " + input;
    }
}
