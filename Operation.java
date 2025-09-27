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

    // Getters and setters
    public double getNumOne() { 
        return numOne; 
    }
    public void setNumOne(double numOne) { 
        this.numOne = numOne; 
    }

    public double getNumTwo() { 
        return numTwo; 
    }
    public void setNumTwo(double numTwo) { 
        this.numTwo = numTwo; 
    }

    public String getOperator() { 
        return operator; 
    }
    public void setOperator(String operator) { 
        this.operator = operator; 
    }

    public double getResult() { 
        return result; 
    }
    public void setResult(double result) { 
        this.result = result; 
    }

    @Override
    public String toString() {
        return String.format("%.1f %s %.1f = %.1f", numOne, operator, numTwo, result);
    }

}
