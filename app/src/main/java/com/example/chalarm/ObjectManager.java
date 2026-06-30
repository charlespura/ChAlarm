package com.example.chalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObjectManager {
    private static final String PREF_NAME = "ObjectManagerPrefs";
    private static final String KEY_OBJECT_COUNT = "object_count";
    private static final String KEY_OBJECT_LABELS = "object_labels";
    private static final String KEY_OBJECT_NAMES = "object_names";
    private static final int MAX_OBJECTS = 15;
    private static final int MIN_OBJECTS = 5;

    private Context context;
    private SharedPreferences prefs;

    public ObjectManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public int getObjectCount() {
        return prefs.getInt(KEY_OBJECT_COUNT, 0);
    }

    public boolean canSetAlarm() {
        return getObjectCount() >= MIN_OBJECTS;
    }

    public boolean canAddObject() {
        return getObjectCount() < MAX_OBJECTS;
    }

    public void addObject(String label, Bitmap image) throws IOException {
        if (!canAddObject()) {
            throw new IllegalStateException("Maximum objects reached");
        }

        int count = getObjectCount();
        int newIndex = count + 1;

        // Save image
        String filename = "target_" + newIndex + ".jpg";
        File file = new File(context.getFilesDir(), filename);
        FileOutputStream fos = new FileOutputStream(file);
        image.compress(Bitmap.CompressFormat.JPEG, 85, fos);
        fos.close();

        // Save label
        Set<String> labels = prefs.getStringSet(KEY_OBJECT_LABELS, new HashSet<>());
        Set<String> names = prefs.getStringSet(KEY_OBJECT_NAMES, new HashSet<>());

        // Convert to mutable sets
        Set<String> mutableLabels = new HashSet<>(labels);
        Set<String> mutableNames = new HashSet<>(names);

        mutableLabels.add(String.valueOf(newIndex));
        mutableNames.add(label);

        prefs.edit()
                .putInt(KEY_OBJECT_COUNT, newIndex)
                .putStringSet(KEY_OBJECT_LABELS, mutableLabels)
                .putStringSet(KEY_OBJECT_NAMES, mutableNames)
                .apply();
    }

    public List<ObjectItem> getObjectItems() {
        List<ObjectItem> items = new ArrayList<>();
        int count = getObjectCount();
        Set<String> labels = prefs.getStringSet(KEY_OBJECT_LABELS, new HashSet<>());
        Set<String> names = prefs.getStringSet(KEY_OBJECT_NAMES, new HashSet<>());

        for (int i = 1; i <= count; i++) {
            String index = String.valueOf(i);
            if (labels.contains(index)) {
                String name = names.contains(index) ?
                        names.stream().filter(s -> s.equals(index)).findFirst().orElse("Item " + i) :
                        "Item " + i;

                File imageFile = new File(context.getFilesDir(), "target_" + i + ".jpg");
                Bitmap image = imageFile.exists() ?
                        BitmapFactory.decodeFile(imageFile.getAbsolutePath()) : null;

                items.add(new ObjectItem(i, name, image));
            }
        }
        return items;
    }

    public Bitmap getObjectImage(int index) {
        String filename = "target_" + index + ".jpg";
        File file = new File(context.getFilesDir(), filename);
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        return null;
    }

    public String getObjectLabel(int index) {
        Set<String> names = prefs.getStringSet(KEY_OBJECT_NAMES, new HashSet<>());
        String indexStr = String.valueOf(index);
        for (String name : names) {
            if (name.startsWith(indexStr + ":")) {
                return name.substring(name.indexOf(":") + 1);
            }
        }
        return "Item " + index;
    }

    public ObjectItem getRandomObject() {
        int count = getObjectCount();
        if (count == 0) return null;

        int randomIndex = (int) (Math.random() * count) + 1;
        String label = getObjectLabel(randomIndex);
        Bitmap image = getObjectImage(randomIndex);

        return new ObjectItem(randomIndex, label, image);
    }

    public static class ObjectItem {
        public int index;
        public String label;
        public Bitmap image;

        public ObjectItem(int index, String label, Bitmap image) {
            this.index = index;
            this.label = label;
            this.image = image;
        }
    }
}