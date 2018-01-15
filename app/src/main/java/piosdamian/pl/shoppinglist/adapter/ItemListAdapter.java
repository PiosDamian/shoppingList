package piosdamian.pl.shoppinglist.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import piosdamian.pl.shoppinglist.R;
import piosdamian.pl.shoppinglist.model.Item;
import piosdamian.pl.shoppinglist.service.ItemService;

/**
 * Created by infinite on 12.01.2018.
 */

public class ItemListAdapter extends BaseAdapter {
    private ItemService items = ItemService.getInstance();
    private LayoutInflater mInflater;

    TextView name, amount, price;


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
        return ((Item) getItem(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_view, parent, false);
        }

        Item item = (Item) getItem(position);

        name = (TextView) convertView.findViewById(R.id.item_name);
        name.setText(item.getName());
        name.setOnClickListener(view -> {
            showPopup((int) getItemId(position), view, parent);
        });

        amount = (TextView) convertView.findViewById(R.id.item_amount);
        amount.setText(Double.toString(item.getAmount()));
        amount.setOnClickListener(view -> {
            showPopup((int) getItemId(position), view, parent);
        });

        price = (TextView) convertView.findViewById(R.id.item_price);
        price.setText(Double.toString(item.getPrice()));
        price.setOnClickListener(view -> {
            showPopup((int) getItemId(position), view, parent);
        });

        return convertView;
    }

    private void showPopup(int position, View view, ViewGroup parent) {
        View popupView = mInflater.inflate(R.layout.edit_view, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);


        TextView label = (TextView) popupView.findViewById(R.id.edit_label);
        EditText input = (EditText) popupView.findViewById(R.id.edit_input);
        Button save = (Button) popupView.findViewById(R.id.button_save);
        Button cancel = (Button) popupView.findViewById(R.id.button_cancel);
        cancel.setOnClickListener(v -> popupWindow.dismiss());

        int viewId = view.getId();
        Context context = view.getContext();
        Item item = (Item) getItem(position);

        switch (viewId) {
            case R.id.item_name:
                label.setText(context.getString(R.string.name));
                input.setText(item.getName());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                save.setOnClickListener(v -> {
                    items.updateName((int) getItemId(position), input.getText().toString());
                    popupWindow.dismiss();
                });
                break;
            case R.id.item_amount:
                label.setText(context.getString(R.string.amount));
                input.setText(Double.toString(item.getAmount()));
                input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                save.setOnClickListener(v -> {
                    items.updateAmount((int) getItemId(position), Double.parseDouble(input.getText().toString()));
                    popupWindow.dismiss();
                });
                break;
            case R.id.item_price:
                label.setText(context.getString(R.string.price));
                input.setText(Double.toString(item.getPrice()));
                input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                save.setOnClickListener(v -> {
                    items.updatePrice((int) getItemId(position), Double.parseDouble(input.getText().toString()));
                    popupWindow.dismiss();
                });
                break;
        }
    }
}
