package piosdamian.pl.shoppinglist.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import piosdamian.pl.shoppinglist.R;
import piosdamian.pl.shoppinglist.observer.Observer;
import piosdamian.pl.shoppinglist.service.file.FileHandler;
import piosdamian.pl.shoppinglist.service.file.FileService;
import piosdamian.pl.shoppinglist.service.item.ItemService;

/**
 * Created by Damian Pioś on 16.01.2018.
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileCardViewHolder> {
    private FileService files;
    private Context context;

    ArrayList<Observer> observers = new ArrayList<>();

    public FileListAdapter(Context context) {
        super();
        this.context = context;
        this.files = FileService.getInstance(this.context);
    }
    @Override
    public FileCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.file_view, parent, false);
        FileCardViewHolder vh = new FileCardViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FileCardViewHolder holder, int position) {
        holder.setCard(files.getFile(position), position, this);
        holder.itemView.setOnClickListener(v -> {
            notify(position);
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public void addObserver(Observer o) {
        observers.add(o);
    }

    private void notify(int pos) {
        for (Observer o: observers) {
            o.update(pos);
        }
    }



    public static class FileCardViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private AppCompatButton fileName;
        private AppCompatButton remove;

        private FileCardViewHolder(LinearLayout itemView) {
            super(itemView);
            this.context = itemView.getContext();
            fileName = itemView.findViewById(R.id.file_name);
            remove = itemView.findViewById(R.id.remove_list);
        }



        private void setCard(String name, int pos, RecyclerView.Adapter adapter) {
            fileName.setText(name);
            remove.setOnClickListener(v -> {
                if(checkIfSafeRemove(name)) {
                    removeFile(pos, adapter);
                } else {
                    createDialog(pos, adapter);
                }
            });
        }

        private boolean checkIfSafeRemove(String filename) {
            ItemService items = ItemService.getInstance();
            items.setItems(FileHandler.readFromFile(context, filename));
            boolean boughtEverything = items.checkIfAllBought();
            items.clearList();
            return boughtEverything;
        }

        private void removeFile(int pos, RecyclerView.Adapter adapter) {
            FileService.getInstance(context).removeFile(pos);
            adapter.notifyDataSetChanged();
        }

        private void createDialog(int pos, RecyclerView.Adapter adapter) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.not_ewerything_bought).setTitle(R.string.remove);

            builder.setPositiveButton("OK", (dialog, which) -> {
                removeFile(pos, adapter);
                dialog.dismiss();
            });

            builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
            dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark));
        }
    }
}
