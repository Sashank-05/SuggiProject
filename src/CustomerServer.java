import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CustomerServer extends Thread {
    private static final int RESTAURANT_PORT = 6000;
    private String customerName;
    private String customerAddress;

    public CustomerServer(String name, String address) {
        this.customerName = name;
        this.customerAddress = address;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        // List of available restaurants (for demo, we hard-code 3 restaurants)
        Restaurant[] restaurants = {
                new Restaurant("Pizza Palace", "Downtown"),
                new Restaurant("Burger King", "Uptown"),
                new Restaurant("Sushi Place", "Midtown")
        };

        try (Socket restaurantSocket = new Socket("localhost", RESTAURANT_PORT);
             ObjectOutputStream outToRestaurant =
                     new ObjectOutputStream(restaurantSocket.getOutputStream());
             ObjectInputStream inFromRestaurant =
                     new ObjectInputStream(restaurantSocket.getInputStream())) {

            System.out.println("Customer Server connected to Restaurant on port " + RESTAURANT_PORT);

            while (true) {
                System.out.println("Available Restaurants:");
                for (int i = 0; i < restaurants.length; i++) {
                    System.out.println(
                            (i + 1) + ". " + restaurants[i].getName() + " - " + restaurants[i].getLocation());
                }
                System.out.println("Choose a restaurant by entering the number:");

                int restaurantChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                if (restaurantChoice < 1 || restaurantChoice > restaurants.length) {
                    System.out.println("Invalid choice, please try again.");
                    continue;
                }

                Restaurant selectedRestaurant = restaurants[restaurantChoice - 1];
                System.out.println("You chose: " + selectedRestaurant.getName());

                System.out.println("Enter your order (e.g., Pizza, Burger): ");
                String foodItems = scanner.nextLine();

                System.out.println("Enter the distance (in km) to your house: ");
                double distance = scanner.nextDouble();
                scanner.nextLine(); // Consume newline character

                // Create an Order object
                Order order =
                        new Order(
                                (int) (Math.random() * 1000),
                                new Customer(customerName, customerAddress),
                                selectedRestaurant,
                                foodItems);

                // Send the order to the restaurant
                Message orderMessage = new Message("ORDER", order.toString());
                outToRestaurant.writeObject(orderMessage);
                System.out.println("Order placed: " + order);

                // Wait for restaurant to accept or reject the order
                Message response = (Message) inFromRestaurant.readObject();
                if ("ACCEPT".equals(response.getType())) {
                    System.out.println("Restaurant accepted the order.");
                    // Inform the customer of estimated delivery time
                    double deliveryTime =
                            distance * 5; // Assuming each delivery driver takes 5 seconds per km
                    System.out.println("Estimated delivery time: " + deliveryTime + " seconds.");
                } else {
                    System.out.println("Restaurant rejected the order.");
                }

                // Ask the customer if they want to place another order
                System.out.println("Do you want to place another order? (yes/no)");
                String choice = scanner.nextLine();
                if (!"yes".equalsIgnoreCase(choice)) {
                    System.out.println("Thank you for using the service. Exiting...");
                    break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
