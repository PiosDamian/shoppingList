package piosdamian.pl.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import piosdamian.pl.shoppinglist.adapter.FileListAdapter;
import piosdamian.pl.shoppinglist.observer.Observer;
import piosdamian.pl.shoppinglist.service.file.FileService;

/**
 * Created by Damian Pioś on 16.01.2018.
 */

public class MainActivity extends AppCompatActivity implements Observer {

    public final static String FILE = "file";

    private AppCompatButton btnNewList;
    private FileService files;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);


        files = FileService.getInstance(MainActivity.this);
        files.setFiles();

        btnNewList = findViewById(R.id.btn_new_list);
        btnNewList.setOnClickListener(v -> {
            popupWindow(v.getContext());
        });

        RecyclerView filesList = findViewById(R.id.files_list);
        filesList.setHasFixedSize(true);
        filesList.setLayoutManager(new LinearLayoutManager(this));
        FileListAdapter adapter = new FileListAdapter(MainActivity.this);
        filesList.setAdapter(adapter);
        adapter.addObserver(this);
    }

    private void popupWindow(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LinearLayout dialogInput = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_input, null);
        TextInputLayout inputArea = dialogInput.findViewById(R.id.input_area);
        TextInputEditText input = dialogInput.findViewById(R.id.dialog_input);

        inputArea.setHint(context.getString(R.string.name));
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setView(dialogInput);

        builder.setPositiveButton(R.string.add_word, (dialog, which) -> {
            if (!files.checkIfEquals(input.getText().toString())) {
                Intent intent = new Intent(context, ListActivity.class);
                intent.putExtra(FILE, input.getText().toString());
                files.addFile(input.getText().toString());
                dialog.dismiss();
                startActivity(intent);
            } else {
                Toast.makeText(context.getApplicationContext(), getString(R.string.name_exist), Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
        input.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lightGreen));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lightOrange));
    }

    @Override
    public void update(Object arg) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(FILE, files.getFile((Integer) arg));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }
}
