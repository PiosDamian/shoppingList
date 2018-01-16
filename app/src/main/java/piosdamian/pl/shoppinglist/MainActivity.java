package piosdamian.pl.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import piosdamian.pl.shoppinglist.adapter.FileListAdapter;
import piosdamian.pl.shoppinglist.service.file.FileService;

/**
 * Created by Damian Pioś on 16.01.2018.
 */

public class MainActivity extends AppCompatActivity {

    public final static String FILE = "file";

    private FileListAdapter adapter;
    private Button btnNewList;
    private FileService files;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        files = FileService.getInstance(MainActivity.this);
        files.setFiles();

        btnNewList = (Button) findViewById(R.id.btn_new_list);
        btnNewList.setOnClickListener(v -> {
            popupWindow(v.getContext());
        });

        ListView filesList = (ListView) findViewById(R.id.files_list);
        adapter = new FileListAdapter(getLayoutInflater(), MainActivity.this);
        filesList.setAdapter(adapter);
        filesList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra(FILE, files.getFile(position));
            startActivity(intent);
        });
    }

    private void popupWindow(Context context) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = mInflater.inflate(R.layout.new_list, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);


        EditText input = (EditText) popupView.findViewById(R.id.new_list_input);
        Button save = (Button) popupView.findViewById(R.id.create_list);
        save.setOnClickListener(v -> {
            if (!files.checkIfEquals(input.getText().toString())) {
                files.addFile(input.getText().toString());
                popupWindow.dismiss();
                startActivity(new Intent(context, ListActivity.class));
            } else {
                Toast.makeText(context.getApplicationContext(), getString(R.string.name_exist), Toast.LENGTH_LONG).show();
            }
        });

        Button cancel = (Button) popupView.findViewById(R.id.cancel_list);
        cancel.setOnClickListener(v -> {
            popupWindow.dismiss();
        });
    }
}
