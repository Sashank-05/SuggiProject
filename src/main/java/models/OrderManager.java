package main.java.models;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class OrderManager {
    private PriorityBlockingQueue<Order> orderQueue;
    private List<DeliveryAgent> agents;
    private Stack<Order> orderHistory;

    public OrderManager() {
        this.orderQueue = new PriorityBlockingQueue<>(10, Comparator.comparing(Order::getOrderTime)); // Orders prioritized by time
        this.agents = new ArrayList<>();
        this.orderHistory = new Stack<>();
    }

    public void addAgent(DeliveryAgent agent) {
        agents.add(agent);
    }

    public void addOrder(Order order) {
        orderQueue.add(order);
    }

    public void assignOrders() {
        for (Order order : orderQueue) {
            for (DeliveryAgent agent : agents) {
                if (agent.isAvailable()) {
                    agent.assignOrder();
                    orderHistory.push(order);
                    System.out.println("Assigned main.java.models.Order " + order.getOrderId() + " to Agent " + agent.getName());
                    break;
                }
            }
        }
    }

    public void completeOrder(DeliveryAgent agent) {
        agent.completeOrder();
        System.out.println("main.java.models.Order completed by " + agent.getName());
    }

    public void printOrderHistory() {
        System.out.println("main.java.models.Order History: ");
        for (Order order : orderHistory) {
            System.out.println(order);
        }
    }
}
