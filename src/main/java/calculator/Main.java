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
        System.out.print("Enter 1 to calculate, 2 to view logs: ");
        int choice = scanner.nextInt();

        if (choice == 1) {
            System.out.print("Enter first number: ");
            int num1 = scanner.nextInt();
            System.out.print("Enter second number: ");
            int num2 = scanner.nextInt();

            CalculationRequest request = CalculationRequest.newBuilder().setNum1(num1).setNum2(num2).build();

            CalculationResponse additionResponse = additionStub.add(request);
            CalculationResponse multiplicationResponse = multiplicationStub.multiply(request);

            System.out.println("Addition Result: " + additionResponse.getResult());
            System.out.println("Multiplication Result: " + multiplicationResponse.getResult());
        } else if (choice == 2) {
            System.out.println("Fetching Addition Logs:");
            additionStub.streamLogs(files_generated.LogRequest.getDefaultInstance())
                    .forEachRemaining(log -> System.out.println(log.getOperation() + " Result: " + log.getResult()));

            System.out.println("Fetching Multiplication Logs:");
            multiplicationStub.streamLogs(files_generated.LogRequest.getDefaultInstance())
                    .forEachRemaining(log -> System.out.println(log.getOperation() + " Result: " + log.getResult()));
        }

        additionChannel.shutdown();
        multiplicationChannel.shutdown();
    }
}
