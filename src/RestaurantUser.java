import java.io.*;
import java.net.*;

public class RestaurantUser extends Thread {
    @Override
    public void run() {
        try (Socket socket = new Socket("localhost", 6000);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            // Send connection type
            out.writeInt(3); // Restaurant connection type

            // Send restaurant details
            String restaurantName = "Restaurant_" + (int) (Math.random() * 100);
            String menu = "Item1, Item2, Item3";
            out.writeUTF(restaurantName);
            out.writeUTF(menu);
            System.out.println("Logged in as restaurant: " + restaurantName);

            // Wait for new orders
            while (true) {
                String message = in.readUTF();
                System.out.println("New message: " + message);
            }
        } catch (IOException e) {
            System.out.println("Error in RestaurantUser: " + e.getMessage());
        }
    }
}
