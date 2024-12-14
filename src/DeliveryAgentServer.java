import java.io.*;
import java.net.*;
import java.util.*;

public class DeliveryAgentServer {
  private static final int RESTAURANT_PORT = 6000; // Port for the restaurant server
  private static final int DELIVERY_DRIVER_COUNT = 3; // Number of delivery drivers

  private static List<DeliveryDriver> drivers = new ArrayList<>();
  private static Socket restaurantSocket;
  private static ObjectInputStream inFromRestaurant;

  public static void main(String[] args) {
    // Initialize delivery drivers
    for (int i = 1; i <= DELIVERY_DRIVER_COUNT; i++) {
      drivers.add(new DeliveryDriver(i));
    }

    // Try to connect to the restaurant server, retrying every 5 seconds if
    // necessary
    while (true) {
      try {
        System.out.println("Attempting to connect to RestaurantServer on port " + RESTAURANT_PORT);
        // Connect to restaurant server
        restaurantSocket = new Socket("localhost", RESTAURANT_PORT);
        inFromRestaurant = new ObjectInputStream(restaurantSocket.getInputStream());

        System.out.println("Connected to RestaurantServer on port " + RESTAURANT_PORT);
        break; // Exit the loop once connected

      } catch (IOException e) {
        System.out.println("Failed to connect to RestaurantServer. Retrying in 5 seconds...");
        try {
          Thread.sleep(5000); // Retry after 5 seconds
        } catch (InterruptedException ie) {
          ie.printStackTrace();
        }
      }
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
          System.out.println("Order assigned to Driver " + availableDriver.getDriverId());
        } else {
          System.out.println("No available drivers. The order will be on hold.");
          // You could implement a retry mechanism here or notify the restaurant
        }
      }
    } catch (IOException | ClassNotFoundException e) {
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
