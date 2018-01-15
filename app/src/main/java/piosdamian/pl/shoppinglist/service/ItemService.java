package piosdamian.pl.shoppinglist.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import piosdamian.pl.shoppinglist.model.Item;

/**
 * Created by infinite on 11.01.2018.
 */

public class ItemService extends Observable {

    static ItemService itemService;

    private ItemService() { }

    public static ItemService getInstance() {
        if (itemService == null) {
            itemService = new ItemService();
        }
        return itemService;
    }

    private double total = 0;
    private List<Item> items = new ArrayList<>();

    public void add(Item item) {
        items.add(item);
        countTotal();
    }

    public Item get(int id) {
        return items.get(id);
    }

    public List<Item> getItems() {
        return items;
    }

    public Iterator<Item> getIterator() {
        return items.iterator();
    }

    public void updateName(int id, String name) {
        getItem(id).setName(name);
    }

    public void updateAmount(int id, double amount) {
        getItem(id).setAmount(amount);
        countTotal();
    }

    public void updatePrice(int id, double price) {
        getItem(id).setPrice(price);
        countTotal();
    }

    private void countTotal() {
        total = 0;
        Iterator<Item> itr = items.iterator();
        while (itr.hasNext()) {
            Item tmp = itr.next();
            total += tmp.getAmount() * tmp.getPrice();
        }
        registerChanges();
    }

    private Item getItem(int id) {
        return items.get(id);
    }

    private void registerChanges() {
        setChanged();
        notifyObservers();
    }

    public double getTotal() {
        return total;
    }
}
