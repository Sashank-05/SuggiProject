package main.java;

import main.java.models.Message;
import main.java.models.Order;
import main.java.models.DeliveryDriver;


import java.io.*;
import java.net.*;
import java.util.*;

public class DeliveryAgentServer extends Thread {
    private static final int RESTAURANT_PORT = 6000; // Port for the restaurant server
    private static final int DELIVERY_DRIVER_COUNT = 3; // Number of delivery drivers

    private static final List<DeliveryDriver> drivers = new ArrayList<>();

    public void run() {
        // Initialize delivery drivers
        for (int i = 1; i <= DELIVERY_DRIVER_COUNT; i++) {
            drivers.add(new DeliveryDriver(i));
        }

        // Try to connect to the restaurant server, retrying every 5 seconds if
        // necessary
        ObjectInputStream inFromRestaurant;
        while (true) {
            System.out.println("Attempting to connect to Restaurant Server on port " + RESTAURANT_PORT);
            // Connect to restaurant server
            try {
                Socket restaurantSocket = new Socket("localhost", RESTAURANT_PORT);
                inFromRestaurant = new ObjectInputStream(restaurantSocket.getInputStream());
            } catch (IOException e) {
                System.out.println("Failed to connect to Restaurant Server. Retrying in 5 seconds...");
                try {
                    Thread.sleep(5000); // Retry after 5 seconds
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                continue;
            }

            System.out.println("Connected to Restaurant Server on port " + RESTAURANT_PORT);
            break; // Exit the loop once connected

        }

        // Once connected, start receiving orders
        try {
            while (true) {
                // Wait for the restaurant to send an order to the delivery agent
                Message orderMessage = (Message) inFromRestaurant.readObject();
                System.out.println("Received order for delivery: " + orderMessage.getContent());

                // Try to assign the order to an available delivery driver
                DeliveryDriver availableDriver = getAvailableDriver();

                if (availableDriver != null) {
                    availableDriver.assignOrder(orderMessage);
                    System.out.println("main.java.models.Order assigned to Driver " + availableDriver.getDriverId());
                } else {
                    System.out.println("No available drivers. The order will be on hold.");
                    // You could implement a retry mechanism here or notify the restaurant
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get the first available driver
    private static DeliveryDriver getAvailableDriver() {
        for (DeliveryDriver driver : drivers) {
            if (!driver.isBusy()) {
                return driver;
            }
        }
        return null; // No available driver
    }
}
