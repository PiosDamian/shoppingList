package piosdamian.pl.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;
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
    private RecyclerView itemsList;
    private NestedScrollView scrollView;

    private TextView total;
    private String listName;

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
        setSupportActionBar(findViewById(R.id.toolbar_list));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        scrollView = findViewById(R.id.itemScrollView);

        Intent intent = getIntent();
        listName = intent.getStringExtra(MainActivity.FILE);
        setTitle(listName);

        itemsList = findViewById(R.id.body);
        itemsList.setHasFixedSize(true);
        itemsList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemListAdapter();
        itemsList.setAdapter(adapter);
        itemsList.setNestedScrollingEnabled(false);

        total = findViewById(R.id.total_amount);
        items.setItems(FileHandler.readFromFile(ListActivity.this, listName));
        if (items.getItems().size() == 0) {
            addItem();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        for (String s : files.getFiles()) {
            if (s.equals(listName))
                continue;
            menu.add(0, Menu.FIRST, Menu.NONE, s);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(MainActivity.FILE, item.getTitle());
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == items && total != null) {
            setTotal();
        }
    }

    public void addItem(View view) {
        addItem();
    }

    private void addItem() {
        Item item = new Item(items.getItems().size(), "", 1, 0);
        items.add(item);
        adapter.notifyDataSetChanged();
        itemsList.smoothScrollToPosition(items.getItems().size());
        scrollView.post(() -> scrollView.smoothScrollTo(0, scrollView.getBottom()));
    }

    private void setTotal() {
        total.post(() -> {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            total.setText(String.format("%.02f", items.getTotal()) + getString(R.string.currency));
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        FileHandler.saveToFile(ListActivity.this, listName, items.getItems());
        super.onPause();
    }
}
