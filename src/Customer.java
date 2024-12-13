public class Customer {
  private String name;
  private String address;

  // Constructor
  public Customer(String name, String address) {
    this.name = name;
    this.address = address;
  }

  // Getters and setters (optional)
  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }

  @Override
  public String toString() {
    return "Customer{name='" + name + "', address='" + address + "'}";
  }
}
