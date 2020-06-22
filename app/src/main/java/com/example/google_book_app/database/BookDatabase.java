package com.example.google_book_app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import timber.log.Timber;

import static com.example.google_book_app.utils.Constants.DATABASE_NAME;

@Database(entities = {BookEntry.class}, version = 1, exportSchema = false)
public abstract class BookDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static BookDatabase sInstance;

    public static BookDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Timber.d("Creating new Room DB instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        BookDatabase.class, DATABASE_NAME)
                        .build();
            }
        }
        Timber.d("Getting Room DB instance");
        return sInstance;
    }

    public abstract BookDao bookDao();
}