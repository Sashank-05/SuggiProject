package models;

public class Restaurant {
    private String name;
    private String menu;

    public Restaurant(String name, String menu) {
        this.name = name;
        this.menu = menu;
    }

    public String getName() {
        return name;
    }

    public String getMenu() {
        return menu;
    }

    @Override
    public String toString() {
        return "Restaurant{name='" + name + "', menu='" + menu + "'}";
    }
}
