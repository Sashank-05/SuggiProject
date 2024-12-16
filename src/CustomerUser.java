import java.io.*;
import java.net.*;

public class CustomerUser extends Thread {
    private final String name;
    private final String location;

    public CustomerUser(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public void run() {
        try (Socket socket = new Socket("localhost", 6000);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            // Send connection type
            out.writeInt(1); // Customer connection type

            // Receive list of restaurants
            int restaurantCount = in.readInt();
            System.out.println("Available restaurants:");
            for (int i = 0; i < restaurantCount; i++) {
                System.out.println(i + ": " + in.readUTF());
            }

            // Select a restaurant
            int selectedRestaurantIndex = (int) (Math.random() * restaurantCount);
            System.out.println("Selected restaurant index: " + selectedRestaurantIndex);
            out.writeInt(selectedRestaurantIndex);

            // Receive menu from the selected restaurant
            String menu = in.readUTF();
            System.out.println("Menu: " + menu);

            // Place an order
            String orderDetails = "Order for: " + name + " (" + location + ")";
            out.writeUTF(orderDetails);
            System.out.println("Order placed: " + orderDetails);

            // Track order status
            while (true) {
                String status = in.readUTF();
                System.out.println(status);
                if (status.contains("delivered")) {
                    break;
                }
                Thread.sleep(2000);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error in CustomerUser: " + e.getMessage());
        }
    }
}
