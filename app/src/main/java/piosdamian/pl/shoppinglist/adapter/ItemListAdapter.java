package piosdamian.pl.shoppinglist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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

import piosdamian.pl.shoppinglist.R;
import piosdamian.pl.shoppinglist.service.item.Item;
import piosdamian.pl.shoppinglist.service.item.ItemService;

/**
 * Created by Damian Pioś on 12.01.2018.
 */

public class ItemListAdapter extends BaseAdapter {
    private ItemService items;
    private LayoutInflater mInflater;
    private Button btnBought, btnRemove;

    private TextView tvName, tvAmount, tvPrice;

    public ItemListAdapter(LayoutInflater inflater) {
        super();
        mInflater = inflater;
        items = ItemService.getInstance();
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

        btnBought = (Button) convertView.findViewById(R.id.button_done);
        btnBought.setOnClickListener(v -> {
            View container = (View) v.getParent().getParent();
            items.updateBoughtState(position, !items.get(position).isBought());
            notifyDataSetChanged();
        });

        btnRemove = (Button) convertView.findViewById(R.id.button_remove);
        btnRemove.setOnClickListener(v -> {
            items.removeItem(item.getId());
            notifyDataSetChanged();
        });

        tvName = (TextView) convertView.findViewById(R.id.item_name);
        tvName.setText(item.getName());
        tvName.setOnClickListener(view -> {
            showPopup((int) getItemId(position), view, parent);
        });

        tvAmount = (TextView) convertView.findViewById(R.id.item_amount);
        tvAmount.setText(Double.toString(item.getAmount()));
        tvAmount.setOnClickListener(view -> {
            showPopup((int) getItemId(position), view, parent);
        });

        tvPrice = (TextView) convertView.findViewById(R.id.item_price);
        tvPrice.setText(Double.toString(item.getPrice()));
        tvPrice.setOnClickListener(view -> {
            showPopup((int) getItemId(position), view, parent);
        });

        if(items.get(position).isBought()) {
            convertView.setBackgroundColor(ContextCompat.getColor(convertView.getContext(), R.color.bought));
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

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
