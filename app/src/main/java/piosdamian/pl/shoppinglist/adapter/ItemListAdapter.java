package piosdamian.pl.shoppinglist.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import piosdamian.pl.shoppinglist.R;
import piosdamian.pl.shoppinglist.service.item.Item;
import piosdamian.pl.shoppinglist.service.item.ItemService;

/**
 * Created by Damian Pioś on 17.01.2018.
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemCardViewHolder> {

    private ItemService items;

    public ItemListAdapter() {
        super();
        this.items = ItemService.getInstance();
    }


    @Override
    public ItemCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        ItemCardViewHolder vh = new ItemCardViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ItemCardViewHolder holder, int position) {
        holder.setCard(position, this);
    }

    @Override
    public int getItemCount() {
        return items.getItems().size();
    }

    public static class ItemCardViewHolder extends RecyclerView.ViewHolder {
        private TextView name, amount, price;
        private Button bought, remove;
        private ItemService items;
        private View itemView;

        private ItemCardViewHolder(View itemView) {
            super(itemView);
            items = ItemService.getInstance();
            this.itemView = itemView;
            name = (TextView) itemView.findViewById(R.id.item_name);
            amount = (TextView) itemView.findViewById(R.id.item_amount);
            price = (TextView) itemView.findViewById(R.id.item_price);
            bought = (Button) itemView.findViewById(R.id.button_item_done);
            remove = (Button) itemView.findViewById(R.id.button_item_remove);
            itemView.findViewById(R.id.edit_area).setBackgroundColor(Color.TRANSPARENT);
        }

        private void setCard(int position, ItemListAdapter adapter) {
            Item item = items.get(position);

            View card = itemView.findViewById(R.id.item_layout);

            if (item.isBought()) {
                card.setBackgroundColor(ContextCompat.getColor(card.getContext(), R.color.bought));
            } else {
                card.setBackgroundColor(Color.TRANSPARENT);
            }

            name.setText(item.getName());
            name.setOnClickListener(view -> {
                showPopup(position, view, adapter);
            });

            amount.setText(String.format("%.02f", item.getAmount()));
            amount.setOnClickListener(view -> {
                showPopup(position, view, adapter);
            });

            price.setText(String.format("%.02f", item.getPrice()));
            price.setOnClickListener(view -> {
                showPopup(position, view, adapter);
            });

            bought.setOnClickListener(v -> {
                items.updateBoughtState(position, !items.get(position).isBought());
                adapter.notifyDataSetChanged();
            });

            remove.setOnClickListener(v -> {
                items.removeItem(position);
                adapter.notifyDataSetChanged();
            });
        }

        private void showPopup(int position, View view, ItemListAdapter adapter) {
            Context context = view.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            EditText input = new EditText(context);
            input.getBackground().mutate().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

            int viewId = view.getId();
            Item item = items.get(position);

            input.setSelectAllOnFocus(true);
            input.setSingleLine(true);

            switch (viewId) {
                case R.id.item_name:
                    builder.setTitle(context.getString(R.string.name));
                    input.setText(item.getName());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setPositiveButton(R.string.save, (dialog, which) -> {
                        items.updateName(position, input.getText().toString());
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    });
                    break;
                case R.id.item_amount:
                    builder.setTitle(context.getString(R.string.amount));
                    input.setText(Double.toString(item.getAmount()));
                    input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    builder.setPositiveButton(R.string.save, (dialog, which) -> {
                        if (commaOccureMoreThanOneTime(input.getText().toString())) {
                            Snackbar.make(view, R.string.wrong_number_format, Snackbar.LENGTH_LONG).show();
                        } else {
                            items.updateAmount(position, Double.parseDouble(input.getText().toString().replace(',', '.')));
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    break;
                case R.id.item_price:
                    builder.setTitle(context.getString(R.string.price));
                    input.setText(Double.toString(item.getPrice()));
                    input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    builder.setPositiveButton(R.string.save, (dialog, which) -> {
                        if (commaOccureMoreThanOneTime(input.getText().toString())) {
                            Snackbar.make(view, R.string.wrong_number_format, Snackbar.LENGTH_LONG).show();
                        } else {
                            items.updatePrice(position, Double.parseDouble(input.getText().toString().replace(',', '.')));
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    break;
            }

            builder.setView(input);
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                dialog.dismiss();
            });

            AlertDialog dialog = builder.create();
            input.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            });
            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
        }

        private boolean commaOccureMoreThanOneTime(String string) {
            int counter = 0;
            for (int i = 0; i < string.length(); i++) {
                if (string.charAt(i) == ',' || string.charAt(i) == '.')
                    counter++;
            }

            return counter > 1;
        }
    }
}
