public class Main {
    public static void main(String[] args) {
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
