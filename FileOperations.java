package org.example;

import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileOperations { // Class name is FileOperations
    private final Gson gson;

    // Regex: Captures (Number) (Operator) (Number), handling optional sign, spaces, and decimals.
    private static final String OPERATION_REGEX =
            "([+\\-]?\\s*\\d*\\.?\\d+)\\s*([+\\-*/])\\s*([+\\-]?\\s*\\d*\\.?\\d+)";
    private static final Pattern OPERATION_PATTERN = Pattern.compile(OPERATION_REGEX);

    public FileOperations() {
        this.gson = new GsonBuilder().setPrettyPrinting().create(); // Saves content as JSON
    }

    // Read operations from input file (Step 1: Read a text file line by line)
    public List<Operation> readOperationsFromFile(String filename) {
        List<Operation> operations = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    Operation operation = parseOperationLine(line.trim()); // Convert them into objects
                    if (operation != null) {
                        operations.add(operation);
                    }
                } catch (Exception e) {
                    // Program should not crash, handle all exceptions
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

    // Parse a single line into an Operation object (Corrected Logic)
    private Operation parseOperationLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        Matcher matcher = OPERATION_PATTERN.matcher(line);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid format: Line must be <num> <op> <num>.");
        }

        try {
            // Group 1: numOne, Group 2: operator, Group 3: numTwo
            double numOne = Double.parseDouble(matcher.group(1).trim());
            String operator = matcher.group(2).trim();
            double numTwo = Double.parseDouble(matcher.group(3).trim());

            // Perform the operation
            double result = calculateResult(numOne, numTwo, operator);

            return new Operation(numOne, numTwo, operator, result);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format.");
        }
    }

    // Calculate the result of the operation
    private double calculateResult(double numOne, double numTwo, String operator) {
        switch (operator) {
            case "+": return numOne + numTwo;
            case "-": return numOne - numTwo;
            case "*": return numOne * numTwo;
            case "/":
                if (numTwo == 0) {
                    throw new ArithmeticException("Division by zero"); // Handle exceptions
                }
                return numOne / numTwo;
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    // Save operations to JSON file (Step 3: Save objects to a new file)
    public void saveOperationsToFile(List<Operation> operations, String filename) {
        // Use a separate class for file related operations
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

    // Read operations from JSON file (Step 4: Read the new file & convert back to objects)
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

    // Display operations in terminal (Step 5: Show the objects as the output)
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
