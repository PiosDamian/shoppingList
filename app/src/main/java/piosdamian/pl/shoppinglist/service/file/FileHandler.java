package piosdamian.pl.shoppinglist.service.file;

import android.app.Activity;
import android.content.Context;
import android.os.Process;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import piosdamian.pl.shoppinglist.service.item.Item;

/**
 * Created by Damian Pioś on 16.01.2018.
 */

public class FileHandler {

    public static void saveToFile(Context context, String fileName, List<?> con) {
        List<?> content = new ArrayList<>(con);
        Thread t = new Thread(() ->{
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            try {
                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(content);
                oos.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.run();
    }

    public static List<Item> readFromFile(Context context, String fileName) {
        List<Item> list = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (ArrayList<Item>) ois.readObject();
            ois.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> filesList(Context context) {
        List<String> list;
        list = Arrays.asList(((Activity) context).getFilesDir().list());
        return list;
    }
}
