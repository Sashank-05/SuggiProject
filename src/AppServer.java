import models.DeliveryAgent;
import models.Order;
import models.Restaurant;

import java.io.*;
import java.net.*;
import java.util.*;

public class AppServer extends Thread {
    private static final int PORT = 6000;
    private static final List<Restaurant> connectedRestaurants = Collections.synchronizedList(new ArrayList<>());
    private static final List<DeliveryAgent> connectedDeliveryAgents = Collections.synchronizedList(new ArrayList<>());
    private static final List<Order> orders = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        new AppServer().start();
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("App Server listening on port " + PORT);

            while (true) {
                Socket userSocket = serverSocket.accept();
                System.out.println("Servicing User " + userSocket.getInetAddress());

                new Thread(() -> handleClient(userSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Error setting up AppServer: " + e.getMessage());
        }
    }

    private void handleClient(Socket userSocket) {
        try (DataInputStream in = new DataInputStream(userSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(userSocket.getOutputStream())) {

            int connectionType = in.readInt();

            switch (connectionType) {
                case 1:
                    handleCustomerRequest(in, out);
                    break;
                case 2:
                    handleDeliveryAgentConnection(in, out);
                    break;
                case 3:
                    handleRestaurantConnection(in, out);
                    break;
                default:
                    out.writeUTF("Invalid connection type.");
                    System.out.println("Invalid connection type received: " + connectionType);
            }
        } catch (IOException e) {
            System.out.println("Error servicing client: " + e.getMessage());
        }
    }

    private void handleDeliveryAgentConnection(DataInputStream in, DataOutputStream out) {
        try {
            String deliveryAgentName = in.readUTF();
            String deliveryAgentLocation = in.readUTF();

            DeliveryAgent deliveryAgent = new DeliveryAgent(deliveryAgentName, deliveryAgentLocation);
            connectedDeliveryAgents.add(deliveryAgent);

            System.out.println("Delivery Agent connected: " + deliveryAgentName);
            out.writeUTF("Connected to App Server. Welcome, " + deliveryAgentName + "!");

            while (true) {
                Order orderToAssign = null;
                synchronized (orders) {
                    for (Order order : orders) {
                        if (order.getDeliveryAgent() == null) {
                            orderToAssign = order;
                            order.setDeliveryAgent(deliveryAgent);
                            break;
                        }
                    }
                }

                if (orderToAssign != null) {
                    out.writeUTF("New Order Received: " + orderToAssign.toString());
                    out.writeUTF("Press 1 to accept the order, 0 to skip.");
                    int response = in.readInt();

                    if (response == 1) {
                        out.writeUTF("Order assigned to you: " + deliveryAgentName);
                        waitForDeliveryCompletion(orderToAssign, in, out);
                        break;
                    } else {
                        orderToAssign.setDeliveryAgent(null);
                    }
                } else {
                    out.writeUTF("No new orders available. Waiting...");
                    Thread.sleep(5000);
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error handling delivery agent: " + e.getMessage());
        }
    }

    private void waitForDeliveryCompletion(Order order, DataInputStream in, DataOutputStream out) throws IOException {
        out.writeUTF("Press 1 to confirm delivery.");
        int confirmation = in.readInt();
        if (confirmation == 1) {
            synchronized (orders) {
                order.setDelivered(true);
                order.setDeliveryAgent(null);
            }
            out.writeUTF("Delivery confirmed. Thank you!");
        }
    }

    private void handleRestaurantConnection(DataInputStream in, DataOutputStream out) {
        try {
            String restaurantName = in.readUTF();
            String menu = in.readUTF();

            Restaurant restaurant = new Restaurant(restaurantName, menu);
            connectedRestaurants.add(restaurant);

            System.out.println("Restaurant connected: " + restaurantName);
            out.writeUTF("Welcome, " + restaurantName + "! Your menu is: " + menu);

            while (true) {
                synchronized (orders) {
                    for (Order order : orders) {
                        if (order.getRestaurant().equals(restaurant) && !order.isNotified()) {
                            out.writeUTF("New order received: " + order.toString());
                            order.setNotified(true);
                        }
                    }
                }
                Thread.sleep(5000);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error handling restaurant connection: " + e.getMessage());
        }
    }

    private void handleCustomerRequest(DataInputStream in, DataOutputStream out) {
        try {
            out.writeInt(connectedRestaurants.size());
            for (Restaurant restaurant : connectedRestaurants) {
                out.writeUTF(restaurant.getName());
            }

            int selectedRestaurantIndex = in.readInt();
            if (selectedRestaurantIndex < 0 || selectedRestaurantIndex >= connectedRestaurants.size()) {
                out.writeUTF("Invalid selection.");
                return;
            }

            Restaurant selectedRestaurant = connectedRestaurants.get(selectedRestaurantIndex);
            out.writeUTF(selectedRestaurant.getMenu());

            String orderDetails = in.readUTF();
            int orderId = (int) (System.currentTimeMillis() / 1000);

            Order newOrder = new Order(orderId, null, selectedRestaurant, orderDetails);
            synchronized (orders) {
                orders.add(newOrder);
            }

            out.writeUTF("Order received. Your order ID is " + orderId);

            while (true) {
                if (newOrder.getDeliveryAgent() != null) {
                    out.writeUTF("Your order is on the way! Delivery Agent: " + newOrder.getDeliveryAgent().toString());
                    break;
                }
                Thread.sleep(2000);
            }

            while (!newOrder.isDelivered()) {
                Thread.sleep(2000);
            }

            out.writeUTF("Your order has been delivered! Thank you for ordering from " + selectedRestaurant.getName());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error handling customer request: " + e.getMessage());
        }
    }
}
