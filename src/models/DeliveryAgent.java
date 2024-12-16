package models;

public class DeliveryAgent {
    private String name;
    private String location;

    public DeliveryAgent(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "DeliveryAgent{name='" + name + "', location='" + location + "'}";
    }
}
