package com.example.google_book_app.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "book")
public class BookEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "book_id")
    private int bookId;

    public int getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
