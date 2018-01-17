package piosdamian.pl.shoppinglist.service.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

/**
 * Created by Damian Pioś on 11.01.2018.
 */

public class ItemService extends Observable {

    static ItemService itemService;
    private double total = 0;
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

    public void setItems(List items) {
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

    public List<Item> getItems() {
        return items;
    }

    public Item get(int id) {
        return items.get(id);
    }

    public double getTotal() {
        return total;
    }

    public boolean checkIfAllBought() {
        for (Item item: items){
            if(!item.isBought()){
                return false;
            }
        }
        return true;
    }

    public Iterator<Item> getIterator() {
        return items.iterator();
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

    private void registerChanges() {
        setChanged();
        notifyObservers();
    }
}
