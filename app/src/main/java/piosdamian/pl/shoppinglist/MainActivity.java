package piosdamian.pl.shoppinglist;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import piosdamian.pl.shoppinglist.adapter.ItemListAdapter;
import piosdamian.pl.shoppinglist.model.Item;
import piosdamian.pl.shoppinglist.service.ItemService;

public class MainActivity extends AppCompatActivity{
    private ItemService items = ItemService.getInstance();
    ItemListAdapter adapter;

    public void addItem(View view) {
        addItem();
    }

    private void addItem() {
        Item item = new Item(items.getItems().size(), "", 1, 0);
        items.add(item);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            return;
        }

        ListView itemList = (ListView) findViewById(R.id.body);
        adapter = new ItemListAdapter(getLayoutInflater());
        itemList.setAdapter(adapter);
        itemList.setOnItemClickListener((parent, view, position, id) -> {
            Item item = (Item) parent.getAdapter().getItem(position);
            Toast.makeText(view.getContext(), Double.toString(item.getAmount()), Toast.LENGTH_LONG).show();
        });
        addItem();
    }
}
