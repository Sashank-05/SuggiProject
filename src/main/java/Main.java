package main.java;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        String[] Names = {"Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy"};
        String[] Locations = {"Downtown", "Uptown", "Midtown", "Eastside", "Westside"};

        // TODO: Start the original Restaurant Server here and create login for restaurants as a different class
        // RestaurantServer restaurantServer = new RestaurantServer();
        // restaurantServer.start();

        System.out.println("  ____  _   _  ____  ____ ___");
        System.out.println(" / ___|| | | |/ ___|/ ___|_ _|");
        System.out.println(" \\___ \\| | | | |  _| |  _ | |");
        System.out.println("  ___) | |_| | |_| | |_| || |");
        System.out.println(" |____/ \\___/ \\____|\\____|___|");
        System.out.println("                             ");


        System.out.println("Welcome to the Food Delivery System!");
        System.out.println("=====================================");
        System.out.println();
        System.out.println("Login as: ");
        System.out.println("c * main.java.models.Customer");
        System.out.println("r * main.java.models.Restaurant");
        System.out.println("d * Delivery Agent");
        System.out.println();
        System.out.print("Enter your choice: ");
        Scanner scanner = new Scanner(System.in);
        String userType = scanner.nextLine();

        switch (userType) {
            case "c":
                System.out.println("Customer login selected.");
                String name = Names[(int) (Math.random() * 10)];
                String location = Locations[(int) (Math.random() * 5)];
                System.out.println("Logged in as: " + name + " from " + location);
                CustomerServer customerServer = new CustomerServer(name, location);
                customerServer.start();
                break;
            case "r":
                // TODO Implement Different restaurants to login and list items
                System.out.println("Restaurant login selected.");
                RestaurantServer restaurantServer = new RestaurantServer();
                restaurantServer.start();
                break;
            case "d":
                System.out.println("Delivery Agent login selected.");
                DeliveryAgentServer deliveryAgentServer = new DeliveryAgentServer();
                deliveryAgentServer.start();
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
                break;
        }


    }
}