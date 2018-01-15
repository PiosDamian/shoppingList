package piosdamian.pl.shoppinglist;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import piosdamian.pl.shoppinglist.adapter.ItemListAdapter;
import piosdamian.pl.shoppinglist.font.FontManager;
import piosdamian.pl.shoppinglist.model.Item;
import piosdamian.pl.shoppinglist.service.ItemService;

public class ListActivity extends AppCompatActivity implements Observer {
    private ItemService items;
    ItemListAdapter adapter;
    TextView total;

    public ListActivity() {
        items = ItemService.getInstance();
        items.addObserver(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Typeface iconFont = FontManager.getTypeFace(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.main), iconFont);
        setContentView(R.layout.activity_list);



        total = (TextView) findViewById(R.id.total_amount);

        if(savedInstanceState != null) {
            return;
        }

        ListView itemList = (ListView) findViewById(R.id.body);
        adapter = new ItemListAdapter(getLayoutInflater());
        itemList.setAdapter(adapter);
        addItem();
    }

    public void addItem(View view) {
        addItem();
    }

    private void addItem() {
        Item item = new Item(items.getItems().size(), "", 1, 0);
        items.add(item);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == items) {
            total.setText(Double.toString(items.getTotal()));
        }
    }
}
