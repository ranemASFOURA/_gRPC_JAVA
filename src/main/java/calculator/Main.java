package calculator;

import files_generated.*;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

// Main class to act as the client for gRPC services
public class Main {
    public static void main(String[] args) {

        // Establish a gRPC channel for the Addition service on localhost and port 50051
        ManagedChannel additionChannel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext() // Use plaintext for non-secure connection (no SSL/TLS)
                .build();

        // Establish a 8 channel for the Multiplication service on localhost and port 50052
        ManagedChannel multiplicationChannel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext() // Use plaintext for non-secure connection
                .build();

        // Create blocking stubs for the Addition and Multiplication services
        AdditionServiceGrpc.AdditionServiceBlockingStub additionStub = AdditionServiceGrpc.newBlockingStub(additionChannel);
        MultiplicationServiceGrpc.MultiplicationServiceBlockingStub multiplicationStub = MultiplicationServiceGrpc.newBlockingStub(multiplicationChannel);

        // Take input from the user for the two numbers
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter first number: ");
        int num1 = scanner.nextInt();
        System.out.print("Enter second number: ");
        int num2 = scanner.nextInt();

        // Create a request object to send to the services, containing the two numbers
        CalculationRequest request = CalculationRequest.newBuilder()
                .setNum1(num1)
                .setNum2(num2)
                .build();

        // Send request to Addition service and receive the response
        CalculationResponse additionResponse = additionStub.add(request);

        // Send request to Multiplication service and receive the response
        CalculationResponse multiplicationResponse = multiplicationStub.multiply(request);

        // Print the results of addition and multiplication to the user
        System.out.println("Addition Result: " + additionResponse.getResult());
        System.out.println("Multiplication Result: " + multiplicationResponse.getResult());

        // Shut down the channels to free resources after usage
        additionChannel.shutdown();
        multiplicationChannel.shutdown();
    }
}
