package main.java.models;

public class DeliveryDriver {
  private final int driverId;
  private boolean isBusy;
  private Message currentOrder;

  public DeliveryDriver(int driverId) {
    this.driverId = driverId;
    this.isBusy = false;
  }

  public int getDriverId() {
    return driverId;
  }

  public boolean isBusy() {
    return isBusy;
  }

  public void assignOrder(Message order) {
    this.isBusy = true;
    this.currentOrder = order;

    // Simulate delivery time based on distance (this is where we use sleep for
    // simulation)
    new Thread(
            () -> {
              try {
                // Assume the order contains the distance in km and simulate the
                // delivery Get distance from the order (you may need to modify your
                // main.java.models.Message class to include this)
                int distance =
                    extractDistance(
                        order.getContent()); // Extract the distance from the message content
                int deliveryTime = distance * 5000; // 5 seconds per km (5000 milliseconds per km)

                System.out.println(
                    "Driver " + driverId + " delivering order: " + order.getContent());
                Thread.sleep(deliveryTime); // Simulate delivery time based on the distance

                System.out.println(
                    "Driver " + driverId + " completed delivery: " + order.getContent());
              } catch (InterruptedException e) {
                e.printStackTrace();
              } finally {
                this.isBusy = false; // Mark driver as available again
              }
            })
        .start();
  }

  // Helper method to extract the distance from the order message (for now,
  // assuming it's in the order content)
  private int extractDistance(String orderContent) {
    // Assuming the order content is like "Pizza, Distance: 5km", we'll extract
    // the distance part
    String[] parts = orderContent.split(", ");
    for (String part : parts) {
      if (part.startsWith("Distance:")) {
        // Extract the distance value
        return Integer.parseInt(part.replace("Distance:", "").trim());
      }
    }
    return 0; // Default to 0 if no distance is found
  }
}
