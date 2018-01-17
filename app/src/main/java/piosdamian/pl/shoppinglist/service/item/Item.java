package piosdamian.pl.shoppinglist.service.item;

import java.io.Serializable;

/**
 * Created by Damian Pioś on 11.01.2018.
 */

public class Item implements Serializable{
    private int id;
    private String name;
    private double amount;
    private double price;
    private boolean bought;

    public Item() {    }

    public Item(int id, String name, double amount, double price) {
        this();
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.bought = false;
    }

    public Item(int id, String name, double amount, double price, boolean bought) {
        this(id, name, amount, price);
        this.bought = bought;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public boolean isBought() {
        return bought;
    }

    void setBought(boolean bought) {
        this.bought = bought;
    }
}
