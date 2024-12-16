package main.java.models;

public class DeliveryAgent {
    private String name;
    private String location;
    private boolean isAvailable;

    public DeliveryAgent(String name, String location) {
        this.name = name;
        this.location = location;
        this.isAvailable = true; // by default, the agent is available
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void assignOrder() {
        this.isAvailable = false; // Assign the order
    }

    public void completeOrder() {
        this.isAvailable = true; // Mark as available after delivery
    }

    @Override
    public String toString() {
        return "main.java.models.DeliveryAgent [name=" + name + ", location=" + location + ", available=" + isAvailable + "]";
    }
}
