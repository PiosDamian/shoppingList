package piosdamian.pl.shoppinglist.service.file;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by infinite on 16.01.2018.
 */

public class FileService {
    private static FileService service;
    private Context context;

    ArrayList<String> files;

    private FileService() {}

    private FileService(Context context) {
        this();
        this.context = context;
        setFiles();
    }

    public static FileService getInstance(Context context) {
        if (service == null) {
            service = new FileService(context);
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
        files = new ArrayList<>(FileHandler.filesList(context));
    }

    public void addFile(String name) {
        files.add(name);
        FileHandler.saveToFile(context, name, new ArrayList<>());

    }

    public void removeFile(int pos) {
        context.deleteFile(files.get(pos));
        files.remove(pos);
    }

    public boolean checkIfEquals(String name) {
        for (String item: files) {
            if (item.equals(name))
                return true;
        }
        return false;
    }

}
