package com.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from Main!");
        if (args.length > 0) {
            System.out.println("Program arguments:");
            for (int i = 0; i < args.length; i++) {
                System.out.printf("  [%d]: %s%n", i, args[i]);
            }
        }
    }
}
