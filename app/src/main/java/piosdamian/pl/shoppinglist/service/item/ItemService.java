package piosdamian.pl.shoppinglist.service.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import lombok.Getter;

/**
 * Created by Damian Pioś on 11.01.2018.
 */

public class ItemService extends Observable {

    private static ItemService itemService;
    @Getter
    private double total = 0;
    @Getter
    private List<Item> items = new ArrayList<>();

    private ItemService() { }

    public static ItemService getInstance() {
        if (itemService == null) {
            itemService = new ItemService();
        }
        return itemService;
    }

    public void add(Item item) {
        items.add(item);
        countTotal();
    }

    public void updateName(int id, String name) {
        get(id).setName(name);
    }

    public void updateAmount(int id, double amount) {
        get(id).setAmount(amount);
        countTotal();
    }

    public void updatePrice(int id, double price) {
        get(id).setPrice(price);
        countTotal();
    }

    public void updateBoughtState(int id, boolean state) {
        get(id).setBought(state);
    }

    public void setItems(List<Item> items) {
        this.items = items;
        countTotal();
    }

    public void removeItem(int id) {
        items.remove(id);
        countTotal();
    }

    public void clearList() {
        items.clear();
    }

    public Item get(int id) {
        return items.get(id);
    }

    public boolean checkIfAllBought() {
        return items.stream().allMatch(Item::isBought);
    }

    private void countTotal() {
        total = 0;
        for (Item tmp : items) {
            total += tmp.getAmount() * tmp.getPrice();
        }
        registerChanges();
    }

    private void registerChanges() {
        setChanged();
        notifyObservers();
    }
}
