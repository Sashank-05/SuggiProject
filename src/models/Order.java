package models;

public class Order {
    private int orderId;
    private DeliveryAgent deliveryAgent;
    private Restaurant restaurant;
    private String details;
    private boolean delivered;
    private boolean notified;

    public Order(int orderId, DeliveryAgent deliveryAgent, Restaurant restaurant, String details) {
        this.orderId = orderId;
        this.deliveryAgent = deliveryAgent;
        this.restaurant = restaurant;
        this.details = details;
        this.delivered = false;
        this.notified = false;
    }

    public int getOrderId() {
        return orderId;
    }

    public DeliveryAgent getDeliveryAgent() {
        return deliveryAgent;
    }

    public void setDeliveryAgent(DeliveryAgent deliveryAgent) {
        this.deliveryAgent = deliveryAgent;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public String getDetails() {
        return details;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    @Override
    public String toString() {
        return "Order{id=" + orderId + ", restaurant=" + restaurant.getName() + ", details='" + details + "'}";
    }
}
