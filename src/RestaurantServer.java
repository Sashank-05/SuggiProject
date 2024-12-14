import java.io.*;
import java.net.*;
import java.util.*;

public class RestaurantServer {
  private static final int PORT = 6000;
  private static final List<DeliveryDriver> deliveryDrivers = new ArrayList<>();
  private static final int MAX_DELIVERY_DRIVERS = 3; // Example: 3 drivers available

  static {
    // Initialize delivery drivers
    for (int i = 0; i < MAX_DELIVERY_DRIVERS; i++) {
      deliveryDrivers.add(new DeliveryDriver(i + 1));
    }
  }

  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      System.out.println("Restaurant Server listening on port " + PORT);

      while (true) {
        try {
          // Wait for a customer connection and handle it in a new thread
          Socket customerSocket = serverSocket.accept();
          new Thread(new CustomerHandler(customerSocket))
              .start(); // Handle customer in a new thread
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      System.out.println("Error setting up RestaurantServer: " + e.getMessage());
    }
  }

  // Inner class to handle each customer connection
  private static class CustomerHandler implements Runnable {
    private Socket customerSocket;

    public CustomerHandler(Socket socket) {
      this.customerSocket = socket;
    }

    @Override
    public void run() {
      try (ObjectInputStream inFromCustomer =
              new ObjectInputStream(customerSocket.getInputStream());
          ObjectOutputStream outToCustomer =
              new ObjectOutputStream(customerSocket.getOutputStream())) {
        System.out.println("Customer connected");

        // Handle incoming order
        Message orderMessage = (Message) inFromCustomer.readObject();
        System.out.println("Received order: " + orderMessage.getContent());

        // Simulate accepting or rejecting the order
        boolean isAccepted = processOrder(orderMessage);

        if (isAccepted) {
          // Send acceptance to customer
          Message acceptMessage = new Message("ACCEPT", "Order accepted by the restaurant.");
          outToCustomer.writeObject(acceptMessage);
          System.out.println("Sent confirmation to customer.");

          // Assign the order to a delivery driver
          assignDeliveryDriver(orderMessage);
        } else {
          // Reject the order
          Message rejectMessage = new Message("REJECT", "Order rejected by the restaurant.");
          outToCustomer.writeObject(rejectMessage);
          System.out.println("Order rejected.");
        }

      } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  // Simulate order processing logic
  private static boolean processOrder(Message orderMessage) {
    // For simplicity, we accept all orders in this example.
    return true;
  }

  // Assign the order to a delivery driver
  private static void assignDeliveryDriver(Message orderMessage) {
    synchronized (deliveryDrivers) {
      // Check if there is an available delivery driver
      for (DeliveryDriver driver : deliveryDrivers) {
        if (!driver.isBusy()) {
          driver.assignOrder(orderMessage);
          System.out.println("Order assigned to driver: " + driver.getDriverId());
          return;
        }
      }
      System.out.println("No available drivers. The order will be queued.");
      // If no driver is available, we could implement queuing logic here.
    }
  }
}
