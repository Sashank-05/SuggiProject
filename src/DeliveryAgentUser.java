import java.io.*;
import java.net.*;
import java.util.Scanner;

public class DeliveryAgentUser extends Thread {
    @Override
    public void run() {
        try (Socket socket = new Socket("localhost", 6000);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            // Send connection type
            out.writeInt(2); // Delivery agent connection type

            // Send delivery agent details
            String agentName = "Agent_" + (int) (Math.random() * 100);
            String agentLocation = "Location_" + (int) (Math.random() * 10);
            out.writeUTF(agentName);
            out.writeUTF(agentLocation);
            System.out.println("Logged in as delivery agent: " + agentName);

            // Wait for orders
            while (true) {
                String message = in.readUTF();
                System.out.println("Message: " + message);

                if (message.startsWith("New Order Received")) {
                    int response = Math.random() > 0.5 ? 1 : 0; // Randomly accept or reject
                    System.out.println("Responding to order with: " + response);
                    out.writeInt(response);
                } else if (message.contains("assigned")) {
                    System.out.println("Order assigned. Confirming delivery...");

                    // wait for scanner to read 1
                    Scanner scanner = new Scanner(System.in);
                    int response = scanner.nextInt();
                    out.writeInt(response);

                    System.out.println("Delivery confirmed.");
                    // read one last message
                    System.out.println(in.readUTF());

                    // dispose current thread and start a new one
                    new DeliveryAgentUser().start();
                }

            }
        } catch (Exception e) {
            System.out.println("Error in DeliveryAgentUser: " + e.getMessage());
        }
    }
}
