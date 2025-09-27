package org.example;

public class Operation {
    private double numOne;
    private double numTwo;
    private String operator;
    private double result;

    // Default constructor
    public Operation() {}

    // Parameterized constructor
    public Operation(double numOne, double numTwo, String operator, double result) {
        this.numOne = numOne;
        this.numTwo = numTwo;
        this.operator = operator;
        this.result = result;
    }

    // Getters for JSON serialization


    public double getNumOne() {
        return numOne;
    }

    public double getNumTwo() {
        return numTwo;
    }

    public String getOperator() {
        return operator;
    }

    public double getResult() {
        return result;
    }

    @Override
    public String toString() {
        // Output format required: 1.0 + 2.0 = 3.0
        return String.format("%.1f %s %.1f = %.1f", numOne, operator, numTwo, result);
    }
}
