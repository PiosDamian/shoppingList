package piosdamian.pl.shoppinglist.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import piosdamian.pl.shoppinglist.R;
import piosdamian.pl.shoppinglist.service.file.FileHandler;
import piosdamian.pl.shoppinglist.service.file.FileService;
import piosdamian.pl.shoppinglist.service.item.ItemService;

/**
 * Created by Damian Pioś on 16.01.2018.
 */

public class FileListAdapter extends BaseAdapter {
    private FileService files;
    private ItemService items;
    private LayoutInflater mInflater;
    private Context context;

    public FileListAdapter (LayoutInflater inflater, Context context) {
        super();
        this.mInflater = inflater;
        this.context = context;
        this.files = FileService.getInstance(context);
        this.items = ItemService.getInstance();
    }


    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.getFile(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.file_view, parent, false);
        }

        TextView tvFileName = (TextView) convertView.findViewById(R.id.file_name);
        tvFileName.setText(files.getFile(position));

        Button removeFile = (Button) convertView.findViewById(R.id.remove_list);
        View finalConvertView = convertView;
        removeFile.setOnClickListener(v -> {
            if (checkIfSafeRemove(files.getFile(position))) {
                removeFile(position);
            } else {
                createDialog(position);
            }
        });



        return convertView;
    }

    private boolean checkIfSafeRemove(String filename) {
        items.setItems(FileHandler.readFromFile(context, filename));
        boolean boughtEverything = items.checkIfAllBought();
        items.clearList();
        return boughtEverything;
    }

    private void removeFile(int pos) {
        files.removeFile(pos);
        notifyDataSetChanged();
    }

    private void createDialog(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder((Activity) context);
        builder.setMessage(R.string.not_ewerything_bought).setTitle(R.string.remove);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeFile(pos);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
