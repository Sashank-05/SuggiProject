import java.util.Date;

public class Order {
    private int orderId;
    private Customer customer;
    private Restaurant restaurant;
    private String foodItems;
    private Date orderTime;

    public Order(int orderId, Customer customer, Restaurant restaurant, String foodItems) {
        this.orderId = orderId;
        this.customer = customer;
        this.restaurant = restaurant;
        this.foodItems = foodItems;
        this.orderTime = new Date();
    }

    public int getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public String getFoodItems() {
        return foodItems;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", customer=" + customer.getName() + ", restaurant=" + restaurant.getName() + ", foodItems=" + foodItems + ", orderTime=" + orderTime + "]";
    }
}
