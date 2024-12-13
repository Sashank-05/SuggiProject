public class Restaurant {
    private String name;
    private String location;

    public Restaurant(String name, String location) {
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
        return "Restaurant [name=" + name + ", location=" + location + "]";
    }
}
