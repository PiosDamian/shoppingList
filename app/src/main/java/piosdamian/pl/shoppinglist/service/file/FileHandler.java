package piosdamian.pl.shoppinglist.service.file;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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

    private static final String DIR = "lists/";
    public static void saveToFile(Context context, String fileName, List<?> content) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(content);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Item> readFromFile(Context context, String fileName) {
        List<Item> list = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (ArrayList<Item>) ois.readObject();
            fis.close();
            ois.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> filesList(Context context, String fileName) {
        List<String> list = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (ArrayList<String>) ois.readObject();
            fis.close();
            ois.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
