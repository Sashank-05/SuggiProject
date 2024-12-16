import models.DeliveryAgent;
import models.Order;
import models.Restaurant;

import java.io.*;
import java.net.*;
import java.util.*;

public class RestaurantServer extends Thread {
    private static final int PORT = 6000;
    private static final List<Restaurant> connectedRestaurants = new ArrayList<>();
    private static final List<DeliveryAgent> connectedDeliveryAgents = new ArrayList<>();
    private static final List<Order> orders = new ArrayList<>();

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("App Server listening on port " + PORT);

            while (true) {
                // Wait for a connection
                Socket userSocket = serverSocket.accept();
                System.out.println("Servicing User " + userSocket.getInetAddress());

                try (DataInputStream in = new DataInputStream(userSocket.getInputStream());
                     DataOutputStream out = new DataOutputStream(userSocket.getOutputStream())) {

                    int connectionType = in.readInt();

                    switch (connectionType) {
                        case 1: // Customer
                            handleCustomerRequest(in, out);
                            break;
                        case 2: // Delivery Agent
                            System.out.println("Delivery Agent connected.");
                            // Add delivery agent to the connected list
                            out.writeUTF("Connected to App Server. Welcome, Delivery Agent!");
                            handleDeliveryAgentConnection(in, out);
                            break;
                        case 3: // Restaurant
                            handleRestaurantConnection(in, out);
                            break;
                        default:
                            System.out.println("Invalid connection type: " + connectionType);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error setting up AppServer: " + e.getMessage());
        }
    }

    private void handleDeliveryAgentConnection(DataInputStream in, DataOutputStream out) {
        new Thread(() -> {
            try {
                // Read delivery agent name
                String deliveryAgentName = in.readUTF();
                String deliveryAgentLocation = in.readUTF();
                // Add delivery agent to the connected list
                DeliveryAgent deliveryAgent = new DeliveryAgent(deliveryAgentName, deliveryAgentLocation);
                connectedDeliveryAgents.add(deliveryAgent);
                System.out.println("Delivery Agent connected: " + deliveryAgentName);

                // Send confirmation to the delivery agent
                out.writeUTF("Connected to App Server. Welcome, " + deliveryAgentName + "!");

                out.writeUTF("Press 1 to receive order the latest order");
                // continuously listen for orders
                boolean isAvailable = true;
                do {
                    for (Order order : orders) {
                        if (order.getDeliveryAgent() == null) {
                            order.deliveryAgent = deliveryAgent;
                            out.writeUTF("Order Recieved: " + order.toString());
                            out.writeUTF("Press 1 to receive order the order, 0 to skip");
                            int x = in.readInt();
                            if (x == 0) {
                                order.deliveryAgent = null;
                                continue;
                            } else {
                                order.deliveryAgent = deliveryAgent;
                                out.writeUTF("Order assigned to (you)" + deliveryAgentName);
                                isAvailable = false;
                                break;

                            }
                        }
                    }
                } while (isAvailable);

                // order is received, now wait for delivery
                out.writeUTF("Press 1 to confirm delivered");
                int x = in.readInt();
                if (x == 1) {
                    for (Order order : orders) {
                        if (order.getDeliveryAgent().equals(deliveryAgent)) {
                            order.deliveryAgent = null;
                            out.writeUTF("Order delivered");
                        }
                    }
                }

            } catch (IOException e) {
                System.out.println("Error handling delivery agent connection: " + e.getMessage());
            }
        }).start();



    }


    private void handleRestaurantConnection(DataInputStream in, DataOutputStream out) {
        new Thread(() -> {
            try {
                int[] orderIDs = new int[100];
                // Read restaurant name and menu
                String restaurantName = in.readUTF();
                // Add restaurant to the connected list
                String menu = in.readUTF();
                Restaurant restaurant = new Restaurant(restaurantName, menu);
                connectedRestaurants.add(restaurant);
                System.out.println("Restaurant connected: " + restaurantName);

                // Send confirmation to the restaurant
                System.out.println("Sending confirmation to " + restaurantName);
                out.writeUTF("Connected to App Server. Welcome, " + restaurantName + "!\n Your menu is: " + menu);
                // continuously listen for orders
                while (true) {
                    for (Order order : orders) {
                        if (order.getRestaurant().getName().equals(restaurantName) && orderIDs[order.getOrderId()] == 0) {
                            int orderID = order.getOrderId();
                            orderIDs[orderID] = orderID;
                            System.out.println("Order received from " + restaurantName + " with order ID: " + orderID);
                            // say restaurant order received
                            out.writeUTF("Order received: " + order.toString());
                        }
                    }
                }

            } catch (IOException e) {
                System.out.println("Error handling restaurant connection: " + e.getMessage());
            }
        }
        ).start();
    }

    private void handleCustomerRequest(DataInputStream in, DataOutputStream out) {
        new Thread(() -> {
            try {
                // Send list of connected restaurants
                out.writeInt(connectedRestaurants.size());
                for (Restaurant restaurant : connectedRestaurants) {
                    out.writeUTF(restaurant.getName());
                }

                // Read customer's selected restaurant
                int selectedRestaurantIndex = in.readInt();
                if (selectedRestaurantIndex < 0 || selectedRestaurantIndex >= connectedRestaurants.size()) {
                    out.writeUTF("Invalid selection.");
                    return;
                }

                // Send menu of the selected restaurant
                Restaurant selectedRestaurant = connectedRestaurants.get(selectedRestaurantIndex);
                out.writeUTF(selectedRestaurant.getMenu());
                System.out.println("Sent menu of " + selectedRestaurant.getName() + " to customer.");

                // Read customer's order
                String order = in.readUTF();
                // generate oid from timestamp
                int orderId = (int) (System.currentTimeMillis() / 1000);
                //int orderId, Customer customer, Restaurant restaurant, String foodItems
                Order newOrder = new Order(orderId, null, selectedRestaurant, order);
                orders.add(newOrder);

                out.writeUTF("Order received. Your order ID is " + orderId);

                while (true) {
                    if (newOrder.getDeliveryAgent() != null) {
                        out.writeUTF("Your order is on the way! Delivery agent: " + newOrder.getDeliveryAgent().toString());
                        break;
                    }
                }
                // wait for delivery
                while (true) {
                    if (newOrder.isDelivered()) {
                        out.writeUTF("Your order has been delivered!");
                        break;
                    }
                }

                // close the socket
                out.writeUTF("Thank you for ordering from " + selectedRestaurant.getName());
                out.writeUTF("Goodbye!");

            } catch (IOException e) {
                System.out.println("Error handling customer request: " + e.getMessage());
            }
        }).start();
    }
}
