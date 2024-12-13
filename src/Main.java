import java.util.*;

class CustomerUI {
    public void run() {
        System.out.println("Customer login selected.");
    }
}


public class Main {
    public static void main(String[] args) {

        String[] Names = {"Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy"};
        String[] Locations = {"Downtown", "Uptown", "Midtown", "Eastside", "Westside"};


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
        System.out.println("c * Customer");
        System.out.println("r * Restaurant");
        System.out.println("d * Delivery Agent");
        System.out.println();
        System.out.print("Enter your choice: ");
        Scanner scanner = new Scanner(System.in);
        String userType = scanner.nextLine();

        switch (userType) {
            case "c":
                System.out.println("Customer login selected.");
                System.out.println("Logged in as: " + Names[(int) (Math.random() * 10)] + " from " + Locations[(int) (Math.random() * 5)]);


                break;
            case "r":
                System.out.println("Restaurant login selected.");

                break;
            case "d":
                System.out.println("Delivery Agent login selected.");

                break;
            default:
                System.out.println("Invalid selection. Please try again.");
                break;
        }

        // Create some customers
        Customer customer1 = new Customer("Alice", "123 Main St");
        Customer customer2 = new Customer("Bob", "456 Oak St");

        // Create some restaurants
        Restaurant restaurant1 = new Restaurant("Pizza Palace", "Downtown");
        Restaurant restaurant2 = new Restaurant("Burger Hut", "Uptown");

        // Create orders
        Order order1 = new Order(1, customer1, restaurant1, "Pizza, Soda");
        Order order2 = new Order(2, customer2, restaurant2, "Burger, Fries");

        // Create delivery agents
        DeliveryAgent agent1 = new DeliveryAgent("John", "Downtown");
        DeliveryAgent agent2 = new DeliveryAgent("Mike", "Uptown");

        // Create the Order Manager
        OrderManager orderManager = new OrderManager();
        orderManager.addAgent(agent1);
        orderManager.addAgent(agent2);

        // Add orders to the system
        orderManager.addOrder(order1);
        orderManager.addOrder(order2);

        // Assign orders
        orderManager.assignOrders();

        // Simulate completing the orders
        orderManager.completeOrder(agent1);
        orderManager.completeOrder(agent2);

        // Print order history
        orderManager.printOrderHistory();
    }
}
