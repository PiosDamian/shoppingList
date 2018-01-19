package piosdamian.pl.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import piosdamian.pl.shoppinglist.adapter.ItemListAdapter;
import piosdamian.pl.shoppinglist.service.file.FileHandler;
import piosdamian.pl.shoppinglist.service.file.FileService;
import piosdamian.pl.shoppinglist.service.item.Item;
import piosdamian.pl.shoppinglist.service.item.ItemService;

public class ListActivity extends AppCompatActivity implements Observer {
    private ItemService items;
    private FileService files;

    private ItemListAdapter adapter;
    private TextView total;
    private RecyclerView itemsList;
    private String listName;

    private Toolbar toolbar;
    private Menu menu;

    public ListActivity() {
        items = ItemService.getInstance();
        items.addObserver(this);

        files = FileService.getInstance(ListActivity.this);
    }

    public ListActivity(String name) {
        this();
        items.setItems(FileHandler.readFromFile(ListActivity.this, name));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar_list);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        listName = intent.getStringExtra(MainActivity.FILE);
        setTitle(listName);

        itemsList = (RecyclerView) findViewById(R.id.body);
        itemsList.setHasFixedSize(true);
        itemsList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemListAdapter();
        itemsList.setAdapter(adapter);

        total = (TextView) findViewById(R.id.total_amount);
        items.setItems(FileHandler.readFromFile(ListActivity.this, listName));
        if (items.getItems().size() == 0) {
            addItem();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_list, this.menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        Intent intent = new Intent(this, ListActivity.class);
//        intent.putExtra(MainActivity.FILE, files.getFile(item.getItemId()));
//        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    public void addItem(View view) {
        addItem();
    }

    private void addItem() {
        Item item = new Item(items.getItems().size(), "", 1, 0);
        items.add(item);
        adapter.notifyDataSetChanged();
        itemsList.smoothScrollToPosition(items.getItems().size());
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == items && total != null) {
            setTotal();
        }
    }

    private void setTotal() {
        total.setText(String.format("%.02f", items.getTotal()).toString() + getString(R.string.currency));
    }

    @Override
    protected void onPause() {
        super.onPause();
        FileHandler.saveToFile(ListActivity.this, listName, items.getItems());
    }
}
