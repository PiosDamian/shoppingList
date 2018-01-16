package piosdamian.pl.shoppinglist.service.file;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by infinite on 16.01.2018.
 */

public class FileService {
    private static final String FILES = "files.json";
    private static FileService service;
    private Context context;

    List<String> files;

    private FileService() {}

    private FileService(Context context) {
        this();
        this.context = context;
        setFiles();
    }

    public static FileService getInstance(Context context) {
        if (service == null) {
            service = new FileService(context.getApplicationContext());
        }
        return service;
    }

    public List<String> getFiles() {
        return files;
    }

    public String getFile (int pos) {
        return files.get(pos);
    }

    public int size() {
        return files.size();
    }

    public void setFiles() {
        files = FileHandler.filesList(context, FILES);
    }

    public void addFile(String name) {
        files.add(name);
        FileHandler.saveToFile(context, FILES, files);
        String[] files = context.getFilesDir().list();
        for(String file: files) {
            Log.d("file", file);
        }
    }

    public void removeFile(int pos) {
        context.deleteFile(files.get(pos));
        files.remove(pos);
        FileHandler.saveToFile(context, FILES, files);
        String[] files = context.getFilesDir().list();
        for(String file: files) {
            Log.d("file", file);
        }
    }

    public boolean checkIfEquals(String name) {
        for (String item: files) {
            if (item.equals(name))
                return true;
        }
        return false;
    }

}
