package piosdamian.pl.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import piosdamian.pl.shoppinglist.adapter.FileListAdapter;
import piosdamian.pl.shoppinglist.observer.Observer;
import piosdamian.pl.shoppinglist.service.file.FileService;

/**
 * Created by Damian Pioś on 16.01.2018.
 */

public class MainActivity extends AppCompatActivity implements Observer {

    public final static String FILE = "file";

    private Button btnNewList;
    private FileService files;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);



        files = FileService.getInstance(MainActivity.this);
        files.setFiles();

        btnNewList = (Button) findViewById(R.id.btn_new_list);
        btnNewList.setOnClickListener(v -> {
            popupWindow(v.getContext());
        });

        RecyclerView filesList = (RecyclerView) findViewById(R.id.files_list);
        filesList.setHasFixedSize(true);
        filesList.setLayoutManager(new LinearLayoutManager(this));
        FileListAdapter adapter = new FileListAdapter(MainActivity.this);
        filesList.setAdapter(adapter);
        adapter.addObserver(this);
    }

    private void popupWindow(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        TextInputLayout inputArea = (TextInputLayout) findViewById(R.id.input_area);
        AppCompatEditText input = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_input, null).findViewById(R.id.dialog_input);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint(R.string.name);
        builder.setView(inputArea);

        builder.setPositiveButton(R.string.add_word, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!files.checkIfEquals(input.getText().toString())) {
                    Intent intent = new Intent(context, ListActivity.class);
                    intent.putExtra(FILE, input.getText().toString());
                    files.addFile(input.getText().toString());
                    dialog.dismiss();
                    startActivity(intent);
                } else {
                    Toast.makeText(context.getApplicationContext(), getString(R.string.name_exist), Toast.LENGTH_LONG).show();
                }
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
        input.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_light));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_orange_dark));
    }

    @Override
    public void update(Object arg) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(FILE, files.getFile((Integer) arg));
        startActivity(intent);
    }
}
