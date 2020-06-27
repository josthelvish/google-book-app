package com.example.google_book_app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface BookDao {

    @Query("SELECT * FROM book")
    LiveData<List<BookEntry>> loadAllBooks();

    @Insert(onConflict = REPLACE)
    void insertBook(BookEntry bookEntry);

    @Delete
    void deleteBook(BookEntry bookEntry);

    @Query("SELECT * FROM book WHERE book_id = :bookId")
    LiveData<BookEntry> loadBookById(String bookId);
}