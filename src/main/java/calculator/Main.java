package calculator;

import files_generated.*;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ManagedChannel additionChannel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        ManagedChannel multiplicationChannel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        AdditionServiceGrpc.AdditionServiceBlockingStub additionStub = AdditionServiceGrpc.newBlockingStub(additionChannel);
        MultiplicationServiceGrpc.MultiplicationServiceBlockingStub multiplicationStub = MultiplicationServiceGrpc.newBlockingStub(multiplicationChannel);

        Scanner scanner = new Scanner(System.in);

        while (true) {  // Start an infinite loop to keep the session active
            System.out.println("Choose an option:");
            System.out.println("1. Perform Calculation");
            System.out.println("2. View Logs");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter first number: ");
                int num1 = scanner.nextInt();
                System.out.print("Enter second number: ");
                int num2 = scanner.nextInt();

                // Prepare the request with user input
                CalculationRequest request = CalculationRequest.newBuilder()
                        .setNum1(num1)
                        .setNum2(num2)
                        .build();

                // Send the request to the addition and multiplication services
                CalculationResponse additionResponse = additionStub.add(request);
                CalculationResponse multiplicationResponse = multiplicationStub.multiply(request);

                // Display results
                System.out.println("Addition Result: " + additionResponse.getResult());
                System.out.println("Multiplication Result: " + multiplicationResponse.getResult());

            } else if (choice == 2) {
                System.out.println("Fetching Addition Logs:");
                additionStub.streamLogs(files_generated.LogRequest.getDefaultInstance())
                        .forEachRemaining(log -> System.out.println("Addition Operation: " + log.getOperation() + " Result: " + log.getResult()));

                System.out.println("Fetching Multiplication Logs:");
                multiplicationStub.streamLogs(files_generated.LogRequest.getDefaultInstance())
                        .forEachRemaining(log -> System.out.println("Multiplication Operation: " + log.getOperation() + " Result: " + log.getResult()));

            } else if (choice == 3) {
                System.out.println("Exiting the program.");
                break;  // Exit the loop, ending the program
            } else {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }

        // Shut down the channels to free resources
        additionChannel.shutdown();
        multiplicationChannel.shutdown();
    }
}
