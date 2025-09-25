package org.example;

import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.Operation;

public class FileOperations {
    private Gson gson;

    public FileOperations() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    // Read operations from input file
    public List<Operation> readOperationsFromFile(String filename) {
        List<Operation> operations = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    Operation operation = parseOperationLine(line.trim());
                    if (operation != null) {
                        operations.add(operation);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + line);
                    System.err.println("Error message: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found: " + filename);
        } catch (IOException e) {
            System.err.println("Error reading file: " + filename);
        }

        return operations;
    }

    // Parse a single line into an Operation object
    private Operation parseOperationLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        try {
            // Split by spaces, but be careful with negative numbers
            String[] parts = line.split("\\s+");

            if (parts.length < 3) {
                throw new IllegalArgumentException("Invalid format: expected <number> <operator> <number>");
            }

            // Handle potential negative numbers
            int operatorIndex = -1;
            String operator = null;

            // Find the operator
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].matches("[+\\-*/]")) {
                    operatorIndex = i;
                    operator = parts[i];
                    break;
                }
            }

            if (operatorIndex == -1) {
                throw new IllegalArgumentException("No valid operator found");
            }

            // Reconstruct numOne from parts before operator
            StringBuilder numOneBuilder = new StringBuilder();
            for (int i = 0; i < operatorIndex; i++) {
                numOneBuilder.append(parts[i]);
            }
            double numOne = Double.parseDouble(numOneBuilder.toString());

            // Reconstruct numTwo from parts after operator
            StringBuilder numTwoBuilder = new StringBuilder();
            for (int i = operatorIndex + 1; i < parts.length; i++) {
                numTwoBuilder.append(parts[i]);
            }
            double numTwo = Double.parseDouble(numTwoBuilder.toString());

            // Calculate result
            double result = calculateResult(numOne, numTwo, operator);

            return new Operation(numOne, numTwo, operator, result);

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse line: " + line + " - " + e.getMessage());
        }
    }

    // Calculate the result of the operation
    private double calculateResult(double numOne, double numTwo, String operator) {
        switch (operator) {
            case "+":
                return numOne + numTwo;
            case "-":
                return numOne - numTwo;
            case "*":
                return numOne * numTwo;
            case "/":
                if (numTwo == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return numOne / numTwo;
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    // Save operations to JSON file
    public void saveOperationsToFile(List<Operation> operations, String filename) {
        if (operations == null || operations.isEmpty()) {
            System.out.println("No operations to save.");
            return;
        }

        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(operations, writer);
            System.out.println("Operations saved to: " + filename);
        } catch (IOException e) {
            System.err.println("Error saving to file: " + filename);
        }
    }

    // Read operations from JSON file
    public List<Operation> readOperationsFromJsonFile(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            java.lang.reflect.Type operationListType = new TypeToken<List<Operation>>(){}.getType();
            return gson.fromJson(reader, operationListType);
        } catch (FileNotFoundException e) {
            System.err.println("JSON file not found: " + filename);
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + filename);
        } catch (Exception e) {
            System.err.println("Error parsing JSON file: " + filename);
        }
        return new ArrayList<>();
    }

    // Display operations in terminal
    public void displayOperations(List<Operation> operations) {
        if (operations == null || operations.isEmpty()) {
            System.out.println("No operations to display.");
            return;
        }

        System.out.println("\nOperations Results:");
        System.out.println("===================");
        for (Operation op : operations) {
            System.out.println(op.toString());
        }
        System.out.println("===================\n");
    }
}