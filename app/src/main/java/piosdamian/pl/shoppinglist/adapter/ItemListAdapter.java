package piosdamian.pl.shoppinglist.adapter;

import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import piosdamian.pl.shoppinglist.R;
import piosdamian.pl.shoppinglist.model.Item;
import piosdamian.pl.shoppinglist.service.ItemService;

/**
 * Created by infinite on 12.01.2018.
 */

public class ItemListAdapter extends BaseAdapter {
    private ItemService items = ItemService.getInstance();
    private LayoutInflater mInflater;

    public ItemListAdapter(LayoutInflater inflater) {
        super();
        mInflater = inflater;
    }

    @Override
    public int getCount() {
        return items.getItems().size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.item_view, parent, false);
        }

        Item item = (Item) getItem(position);

        EditText name = (EditText) convertView.findViewById((R.id.item_name));
        name.setText(item.getName());

        name.setOnFocusChangeListener((v, hasFocus) -> {
            Toast.makeText(v.getContext(), Integer.toString(v.getId()), Toast.LENGTH_LONG).show();
        });

        EditText amount = (EditText) convertView.findViewById(R.id.item_amount);
        amount.setText(Double.toString(item.getAmount()));

        EditText price = (EditText) convertView.findViewById(R.id.item_price);
        price.setText(Double.toString(item.getPrice()));

        return convertView;
    }


}
