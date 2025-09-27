package org.example;

import java.util.List;

public class Main { // Class name is Main
    public static void main(String[] args) {
        // File paths
        String inputFile = "operations.txt";
        String outputFile = "results.json";

        // Create file operations instance
        FileOperations fileOps = new FileOperations();

        try {
            System.out.println("Reading operations from: " + inputFile);

            // Step 1: Read and process operations
            List<Operation> operations = fileOps.readOperationsFromFile(inputFile);

            if (operations.isEmpty()) {
                System.out.println("No valid operations found in the input file.");
                return;
            }

            System.out.println("Successfully parsed " + operations.size() + " operations.");

            // Step 2: Save operations to JSON file
            fileOps.saveOperationsToFile(operations, outputFile);

            // Step 3: Read back from JSON file
            System.out.println("Reading operations from JSON file: " + outputFile);
            List<Operation> loadedOperations = fileOps.readOperationsFromJsonFile(outputFile);

            // Step 4: Display operations in terminal
            fileOps.displayOperations(loadedOperations);

        } catch (Exception e) {
            System.err.println("Unexpected error in main program: " + e.getMessage());
        }
    }
}
