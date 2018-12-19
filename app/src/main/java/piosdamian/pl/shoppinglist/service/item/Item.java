package piosdamian.pl.shoppinglist.service.item;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by Damian Pioś on 11.01.2018.
 */

@Data
public class Item implements Serializable {
    private int id;
    private String name;
    private double amount;
    private double price;
    private boolean bought;

    public Item(int id, String name, double amount, double price) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.bought = false;
    }
}
